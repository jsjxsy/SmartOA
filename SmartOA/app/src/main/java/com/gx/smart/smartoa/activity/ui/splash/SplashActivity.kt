package com.gx.smart.smartoa.activity.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.activity.ui.guide.IntroActivity
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.lib.base.BaseActivity
import com.gx.smart.common.AppConfig
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.AppMessagePushService
import com.gx.smart.smartoa.data.network.api.AuthApiService
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.push.UpdateMessagePushResponse

class SplashActivity : BaseActivity() {

    private var loginTask: GrpcAsyncTask<String, Void, LoginResp>? = null
    private var loginCallBack: CallBack<LoginResp?>? = null

    private var bindTask: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var bindCallBack: CallBack<AppInfoResponse?>? = null

    //权限列表
    var permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.DISABLE_KEYGUARD,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    var mPermissionList: MutableList<String> = arrayListOf()
    private val mRequestCode: Int = 100 //权限请求码

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)//恢复原有的样式
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        requestPermission()
    }

    private fun requestPermission() { //判断哪些权限未授予
        mPermissionList.clear()
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permissions.get(i)
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionList.add(permissions.get(i))
            }
        }
        //申请权限
        if (mPermissionList.size > 0) { //有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode)
        } else { //说明权限都已经通过，可以做你想做的事情去
            startEnter()
        }
    }

    private fun startEnter() {
        val first = SPUtils.getInstance().getBoolean(AppConfig.SH_FIRST_OPEN, true)
        if (first) {
            startActivity(Intent(this, IntroActivity::class.java))
            SPUtils.getInstance().put(AppConfig.SH_FIRST_OPEN, false)
            finish()
        } else {
            val splashFragment = supportFragmentManager.findFragmentById(R.id.splashFragment)
            (splashFragment as SplashFragment).showAd()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermissionDismiss = false //有权限没有通过
        if (mRequestCode == requestCode) {
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) { //重新申请
                ActivityCompat.requestPermissions(this, permissions, mRequestCode)
            } else {
                startEnter()
            }
        }
    }


    fun updateMessagePushAndLogin() {
        loginAuto()
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


    private fun loginActivity() {
        finish()
        val intent = Intent(SplashActivity@ this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ActivityUtils.startActivity(intent)
    }

    private fun loginAuto() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            loginActivity()
        } else {
            val userName = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
            val password = SPUtils.getInstance().getString(AppConfig.SH_PASSWORD, "")
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                //手机号密码登录
                val loginType = 2
                loginResponseCallBack(true, userName, password)
                if (GrpcAsyncTask.isFinish(loginTask)) {
                    loginTask = AuthApiService.getInstance()
                        .login(userName, password, loginType, loginCallBack)
                }
            } else {
                loginActivity()
            }

        }
    }


    /*******************************************登录回调 */
    private fun loginResponseCallBack(
        isPassWord: Boolean,
        userName: String?,
        password: String?
    ) {
        loginCallBack = object : CallBack<LoginResp?>() {
            override fun callBack(result: LoginResp?) {
                if (result == null) {
                    ToastUtils.showLong("登录超时")
                    LoginActivity()
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    SPUtils.getInstance().put(AppConfig.LOGIN_TOKEN, result.token)
                    //保存当前用户
                    if (isPassWord) { //保存当前用户
                        SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, userName)
                        SPUtils.getInstance().put(AppConfig.SH_PASSWORD, password)
                    } else {
                        SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, userName)
                    }
                    updateMessagePush()
                    bindAppCallBack()
                    if (GrpcAsyncTask.isFinish(bindTask)) {
                        bindTask =
                            UserCenterService.getInstance()
                                .bindAppUser(userName, userName, bindCallBack)
                    }
                } else {
                    ToastUtils.showLong(msg)
                    loginActivity()
                }
            }
        }
    }


    /*******************************************绑定回调 */
    fun bindAppCallBack() {
        bindCallBack = object : CallBack<AppInfoResponse?>() {
            override fun callBack(result: AppInfoResponse?) {
                if (result == null) {
                    ToastUtils.showLong("登录后绑定超时")
                    return
                }

                when (result.code) {
                    100 -> {
                        SPUtils.getInstance().put(AppConfig.USER_ID, result.appUserInfoDto.userId)
                        myCompany()
                    }
                    //用户已经绑定
                    7003 -> {
                        SPUtils.getInstance().put(AppConfig.USER_ID, result.appUserInfoDto.userId)
                        myCompany()
                    }
                    else -> {
                        ToastUtils.showLong(result.msg)
                        loginActivity()
                    }
                }
            }
        }
    }


    private fun mainActivity() {
        finish()
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        ActivityUtils.startActivity(
            intent
        )

    }


    private fun mineCompanyActivity() {
        val intent = Intent(
            this,
            MineCompanyActivity::class.java
        )
        intent.putExtra(MineCompanyActivity.FROM_SPLASH, MineCompanyActivity.FROM_SPLASH)
        ActivityUtils.startActivity(
            intent
        )
        finish()
    }


    private fun myCompany() {
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("查询我的企业超时!")
                            return
                        }
                        if (result?.code == 100) {
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
                                    .put(AppConfig.PLACE_NAME, employeeInfo.buildingName)
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
                                }
                                mainActivity()
                            } else {
                                val tenantNo = SPUtils.getInstance()
                                    .getInt(AppConfig.BUILDING_SYS_TENANT_NO, 0)
                                if (tenantNo == 0) {
                                    mineCompanyActivity()
                                } else {
                                    mainActivity()
                                }

                            }

                        } else {
                            ToastUtils.showLong(result.msg)
                            loginActivity()
                        }
                    }

                })
    }


    companion object {
        const val DELAY_TIME: Long = 1000 * 3
    }


}
