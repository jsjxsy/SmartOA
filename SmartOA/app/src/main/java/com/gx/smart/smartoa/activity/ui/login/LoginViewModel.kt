package com.gx.smart.smartoa.activity.ui.login

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.LoginRepository
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AppEmployeeService
import com.gx.smart.lib.http.api.AppMessagePushService
import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.push.UpdateMessagePushResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    var phone = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var verifyCodeCallBackSuccess = MutableLiveData<Boolean>(false)
    var isLoading = MutableLiveData<Boolean>()
    var deleteVisible = MutableLiveData<Boolean>()
    var targetPage = MutableLiveData<Int>()

    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null

    private var loginTask: GrpcAsyncTask<String, Void, LoginResp>? = null
    private var loginCallBack: CallBack<LoginResp?>? = null

    private var bindTask: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var bindCallBack: CallBack<AppInfoResponse?>? = null

    enum class LoginTypeEnum {
        PHONE, VERIFY_CODE
    }

    var loginFlag = MutableLiveData<LoginTypeEnum>(LoginTypeEnum.PHONE)
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

    fun loginType() {
        when (loginFlag.value) {
            LoginTypeEnum.PHONE ->
                loginFlag.value = LoginTypeEnum.VERIFY_CODE

            LoginTypeEnum.VERIFY_CODE -> {
                loginFlag.value = LoginTypeEnum.PHONE
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

        when (loginFlag.value) {
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
     * 登陆
     * @param loginType 2:手机号密码登录 3: 手机号验证码登录
     */
    fun login(loginType: Int) {
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
                bindAppCallBack()
                if (GrpcAsyncTask.isFinish(bindTask)) {
                    bindTask =
                        UserCenterService.getInstance()
                            .bindAppUser(phone.value!!, phone.value!!, bindCallBack)
                }
            } else {
                isLoading.value = false
                ToastUtils.showLong(msg)
            }
        }
            , {
                ToastUtils.showLong("登录超时")
            })

        if (GrpcAsyncTask.isFinish(loginTask)) {
            loginTask = AuthApiService.getInstance()
                .login(phone.value, password.value, loginType, loginCallBack)
        }
    }


    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                error(e)
            }
        }


    /*******************************************绑定回调 */
    private fun bindAppCallBack() {
        bindCallBack = object : CallBack<AppInfoResponse?>() {
            override fun callBack(result: AppInfoResponse?) {
                if (result == null) {
                    ToastUtils.showLong("登录后绑定超时")
                    isLoading.value = false
                    return
                }

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
            }
        }
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
                        Log.i("jtpush", result.toString())
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

        verifyCodeCallBack()
        if (GrpcAsyncTask.isFinish(verifyTask)) {
            verifyTask =
                AuthApiService.getInstance()
                    .verifyCode(phone.value, targetType, purpose, verifyCallBack)
        }


    }

    override fun toString(): String {
        return "phone $phone password $password"
    }


    /*******************************************获取验证码回调 */
    private fun verifyCodeCallBack() {
        verifyCallBack = object : CallBack<VerifyCodeResp?>() {
            override fun callBack(result: VerifyCodeResp?) {

                if (result == null) {
                    ToastUtils.showLong("验证码请求超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    verifyCodeCallBackSuccess.value = true
                    ToastUtils.showLong("获取验证码成功")
                } else {
                    ToastUtils.showLong(msg)
                    isLoading.value = false
                }
            }
        }
    }
}
