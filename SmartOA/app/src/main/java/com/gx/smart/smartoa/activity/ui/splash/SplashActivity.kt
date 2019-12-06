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
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.smartoa.base.BaseActivity
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppMessagePushService
import com.gx.smart.smartoa.data.network.api.AuthApiService
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
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
        Manifest.permission.DISABLE_KEYGUARD
    )
    var mPermissionList: MutableList<String> = arrayListOf()
    private val mRequestCode: Int = 100 //权限请求码

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val splashFragment = supportFragmentManager.findFragmentById(R.id.splashFragment)
        (splashFragment as SplashFragment).showAd()
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
        //上传极光ID
        if (null != AppConfig.mJiGuangToekn) {
            AppMessagePushService.getInstance().updateMessagePush(
                AppConfig.mJiGuangToekn,
                object : CallBack<UpdateMessagePushResponse?>() {
                    override fun callBack(result: UpdateMessagePushResponse?) {
                        Log.i("jtpush", result.toString())
                    }
                })
        }
        loginAuto()
    }


    private fun loginActivity() {
        finish()
        startActivity(Intent(SplashActivity@ this, LoginActivity::class.java))
    }

    private fun loginAuto() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            loginActivity()
        } else {
            val userName = SPUtils.getInstance().getString(AppConfig.SH_USERNAME, "")
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
                    AppConfig.loginToken = result.token
                    AppConfig.refreshToken = result.refreshToken
                    //保存当前用户
                    if (isPassWord) { //保存当前用户
                        SPUtils.getInstance().put(AppConfig.SH_USERNAME, userName)
                        SPUtils.getInstance().put(AppConfig.SH_PASSWORD, password)
                    } else {
                        SPUtils.getInstance().put(AppConfig.SH_USERNAME, userName)
                    }
                    bindAppCallBack()
                    if (GrpcAsyncTask.isFinish(bindTask)) {
                        bindTask =
                            UserCenterService.getInstance()
                                .bindAppUser(userName, userName, bindCallBack)
                    }
                } else {
                    ToastUtils.showLong(msg)
                    LoginActivity()
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
                        ActivityUtils.startActivity(
                            Intent(
                                this@SplashActivity,
                                MainActivity::class.java
                            )
                        )
                        //用户已经绑定
                    }
                    7003 -> {
                        ActivityUtils.startActivity(
                            Intent(
                                this@SplashActivity,
                                MainActivity::class.java
                            )
                        )
                    }
                    else -> {
                        ToastUtils.showLong(result.msg)
                        LoginActivity()
                    }
                }
            }
        }
    }


    companion object {
        const val DELAY_TIME: Long = 1000 * 3
    }
}
