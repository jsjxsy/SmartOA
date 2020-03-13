package com.gx.smart.module.login.mvvm.viewmodel

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.common.AppConfig
import com.gx.smart.common.DataCheckUtil
import com.gx.smart.lib.http.api.AppEmployeeService
import com.gx.smart.lib.http.api.AppMessagePushService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.module.login.R
import com.gx.smart.module.login.fragment.ForgetPasswordFragment
import com.gx.smart.module.login.mvvm.repository.LoginRepository
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.push.UpdateMessagePushResponse
import com.orhanobut.logger.Logger

class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {
    var phone = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var verifyCodeCallBackSuccess = MutableLiveData<Boolean>(false)
    var isLoading = MutableLiveData<Boolean>()
    var deleteVisible = MutableLiveData<Boolean>()
    var targetPage = MutableLiveData<Int>()
    var passwordState = MutableLiveData<Boolean>(false) //true: 密码可见 false:不可见

    /**
     * 手机登陆或者验证码登陆
     */
    enum class LoginTypeEnum {
        PHONE, VERIFY_CODE
    }

    var loginTypeFlag = MutableLiveData(
        LoginTypeEnum.PHONE
    )

    //获取登录验证码
    private val targetType = 1
    private val purpose = 1

    fun setPhone() {
        val account = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        phone.value = account
        deleteVisible.value = account.isNotEmpty()
    }

    fun clearPhone() {
        phone.value = ""
    }

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val length = s?.length ?: 0
            deleteVisible.value = length > 0
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    fun register(v: View) {
        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun passwordStateSwitch() {
        passwordState.value = !(passwordState.value != null && passwordState.value == true)
    }

    /**
     * 手机获取焦点的时候
     */
    fun phoneEditText(v: EditText, focus: Boolean) {
        if (focus) {
            v.setSelection(
                phone.value?.length ?: 0
            )
        }
    }

    fun forgetPassword(v: View) {
        val userName = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showLong("请先注册")
            Navigation.findNavController(v)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        } else {
            val bundle = Bundle()
            bundle.putString(ForgetPasswordFragment.ARG_PHONE_NUMBER, userName)
            Navigation.findNavController(v)
                .navigate(R.id.action_loginFragment_to_forgetPasswordFragment, bundle)
        }
    }

    fun loginType() {
        when (loginTypeFlag.value) {
            LoginTypeEnum.PHONE ->
                loginTypeFlag.value =
                    LoginTypeEnum.VERIFY_CODE

            LoginTypeEnum.VERIFY_CODE -> {
                loginTypeFlag.value =
                    LoginTypeEnum.PHONE
            }
        }
    }


    fun login() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }

        val password: String = password.value!!.trim()
        if (TextUtils.isEmpty(phone.value)) {
            ToastUtils.showLong("手机号不能为空")
            return
        }
        if (phone.value?.length != 11 || !DataCheckUtil.isMobile(phone.value)) {
            ToastUtils.showLong("非法手机号")
            return
        }

        when (loginTypeFlag.value) {
            LoginTypeEnum.VERIFY_CODE -> {
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong("验证码不能为空")
                    return
                }
                login(3)
            }
            LoginTypeEnum.PHONE -> {

                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong("密码不能为空")
                    return
                }
                login(2)
            }

        }

        isLoading.value = true
    }

    /**
     * 登陆 网络请求
     * @param loginType 2:手机号密码登录 3: 手机号验证码登录
     */
    private fun login(loginType: Int) {
        launch({
            val result = loginRepository.login(phone.value!!, password.value!!, loginType)
            val msg = result.dataMap["errMsg"]
            if (result.code == 100) {
                SPUtils.getInstance().put(AppConfig.LOGIN_TOKEN, result.token)
                //保存当前用户
                val isPassWord = loginType == 2
                if (isPassWord) {
                    SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone.value)
                    SPUtils.getInstance().put(AppConfig.SH_PASSWORD, password.value)
                } else {
                    SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone.value)
                }
                updateMessagePush()
                bindAppUser(phone.value!!)
            } else {
                isLoading.value = false
                ToastUtils.showLong(msg)
            }
        }
            , {
                ToastUtils.showLong("登录超时")
            })

    }


    private fun bindAppUser(phone: String) {
        launch({
            val result = loginRepository.bindAppUser(phone)
            when {
                result.code === 100 -> {
                    SPUtils.getInstance().put(AppConfig.USER_ID, result.appUserInfoDto.userId)
                    myCompany()
                    //用户已经绑定
                }
                result.code == 7003 -> {
                    SPUtils.getInstance().put(AppConfig.USER_ID, result.appUserInfoDto.userId)
                    myCompany()
                }
                else -> {
                    ToastUtils.showLong(result.msg)
                    isLoading.value = false
                }
            }
        }, {
            ToastUtils.showLong("登录后绑定超时")
            isLoading.value = false
        })
    }

    private fun myCompany() {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        isLoading.value = false

                        if (result == null) {
                            ToastUtils.showLong("查询我的企业超时!")
                            return
                        }
                        if (result.code == 100) {
                            val employeeList = result.employeeInfoList
                            if (employeeList.isNotEmpty()) {
                                val employeeInfo = employeeList[0]
                                SPUtils.getInstance()
                                    .put(AppConfig.EMPLOYEE_ID, employeeInfo.employeeId)
                                SPUtils.getInstance()
                                    .put(
                                        AppConfig.COMPANY_STRUCTURE_ID,
                                        employeeInfo.companyStructureId
                                    )
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_SYS_TENANT_NO, employeeInfo.tenantNo)
                                SPUtils.getInstance()
                                    .put(
                                        AppConfig.SMART_HOME_SN,
                                        employeeInfo.appDepartmentInfo.smartHomeSn
                                    )
                                SPUtils.getInstance()
                                    .put(
                                        AppConfig.ROOM_ID,
                                        employeeInfo.appDepartmentInfo.smartHomeId
                                    )
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_PLACE_NAME, employeeInfo.buildingName)
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_NAME, employeeInfo.companyName)
                                SPUtils.getInstance()
                                    .put(AppConfig.COMPANY_APPLY_STATUS, employeeInfo.status)
                                val tenantNo = SPUtils.getInstance()
                                    .getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
                                if (tenantNo == 0) {
                                    SPUtils.getInstance()
                                        .put(
                                            AppConfig.BUILDING_SYS_TENANT_NO,
                                            employeeInfo.tenantNo
                                        )
                                    SPUtils.getInstance()
                                        .put(AppConfig.PLACE_NAME, employeeInfo.companyName)
                                }
                                targetPage.value = 1
                            } else {
                                val tenantNo = SPUtils.getInstance()
                                    .getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
                                if (tenantNo == 0) {
                                    targetPage.value = 2
                                } else {
                                    targetPage.value = 1
                                }
                            }
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }


    private fun updateMessagePush() {
        //上传极光ID
        if (null != AppConfig.JGToken) {
            AppMessagePushService.getInstance().updateMessagePush(
                AppConfig.JGToken,
                object : CallBack<UpdateMessagePushResponse?>() {
                    override fun callBack(result: UpdateMessagePushResponse?) {
                        Logger.d(result)
                    }
                })
        }
    }


    fun getVerifyCode() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        if (TextUtils.isEmpty(phone.value)) {
            ToastUtils.showLong("手机号不能为空")
            return
        }
        if (phone.value?.length != 11 || !DataCheckUtil.isMobile(phone.value)) {
            ToastUtils.showLong("非法手机号")
            return
        }

        verifyCode(phone.value!!, targetType, purpose)

    }

    private fun verifyCode(phone: String, targetType: Int, purpose: Int) {
        launch({
            val result = loginRepository.verify(phone, targetType, purpose)
            val msg = result.dataMap["errMsg"]
            if (result.code == 100) {
                verifyCodeCallBackSuccess.value = true
                ToastUtils.showLong("获取验证码成功")
            } else {
                ToastUtils.showLong(msg)
                isLoading.value = false
            }
        }, {
            ToastUtils.showLong("验证码请求超时")
        })

    }


}
