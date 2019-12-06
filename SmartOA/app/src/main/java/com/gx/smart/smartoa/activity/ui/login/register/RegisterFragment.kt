package com.gx.smart.smartoa.activity.ui.login.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.smartoa.activity.ui.splash.SplashActivity.Companion.DELAY_TIME
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AuthApiService
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.smart.smartoa.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.RegistResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : Fragment(), View.OnClickListener {

    companion object {

        fun newInstance() =
            RegisterFragment()
    }


    private lateinit var viewModel: RegisterViewModel

    private var registerBack: CallBack<RegistResp?>? = null
    private var authTask: GrpcAsyncTask<String, Void, RegistResp>? = null

    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null

    private var loginTask: GrpcAsyncTask<String, Void, LoginResp>? = null
    private var loginCallBack: CallBack<LoginResp?>? = null

    private var bindTask: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var bindCallBack: CallBack<AppInfoResponse?>? = null

    private var mTime: TimeCount? = null
    private var mPhone: String? = null
    private var passWord: String? = null
    private lateinit var verifyCodeText: TextView
    private lateinit var mLoadingView: LoadingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        verifyCodeText = getVerifyCodeText
        mLoadingView = loadingView
        initTitle()
        initData()
        register.setOnClickListener(this)
        getVerifyCodeText.setOnClickListener(this)
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.register_account)
        }
        left_nav_image_view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        mPhone = phoneText.text.toString().trim()
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.register -> {
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showLong("网络连接不可用")
                    return
                }
                val password: String = newPassword.text.toString().trim()
                val confirmPassword: String =
                    confirmNewPassword.text.toString().trim()
                val identityCode: String =
                    verifyCode.text.toString().trim()
                if (TextUtils.isEmpty(mPhone)) {
                    ToastUtils.showLong("手机号不能为空")
                } else if (mPhone!!.length != 11 || !DataCheckUtil.isMobile(mPhone)) {
                    ToastUtils.showLong("非法手机号")
                } else if (TextUtils.isEmpty(identityCode)) {
                    ToastUtils.showLong("验证码不能为空")
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong("密码不能为空")
                } else if (password.length < 6 || password.length > 16) {
                    ToastUtils.showLong("密码长度不得小于6位大于16位")
                } else if (doExcute(password) < 2) {
                    ToastUtils.showLong("密码格式不正确")
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    ToastUtils.showLong("确认密码不能为空")
                } else if (password != confirmPassword) {
                    ToastUtils.showLong("确认密码与密码不一致")
                } else {
                    mLoadingView.setVisibility(View.VISIBLE)
                    mLoadingView.setOnClickListener(null)
                    mLoadingView.setText("注册中")
                    mLoadingView.showLoading()
                    mLoadingView.postDelayed(
                        { mLoadingView.setVisibility(View.GONE) },
                        DELAY_TIME
                    )
                    passWord = password
                    appUserRegisterCallBack()
                    if (GrpcAsyncTask.isFinish(authTask)) {
                        authTask = AuthApiService.getInstance()
                            .regist(mPhone, password, mPhone, identityCode, registerBack)
                    }
                }
            }
            R.id.getVerifyCodeText -> {
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showLong("网络连接不可用")
                    return
                }
                if (TextUtils.isEmpty(mPhone)) {
                    ToastUtils.showLong("手机号不能为空")
                } else if (mPhone!!.length != 11 || !DataCheckUtil.isMobile(mPhone)) {
                    ToastUtils.showLong("非法手机号")
                } else {
                    getVerifyCode()
                    val targetType = 1
                    val purpose = 2
                    if (GrpcAsyncTask.isFinish(verifyTask)) {
                        verifyTask = AuthApiService.getInstance()
                            .verifyCode(mPhone, targetType, purpose, verifyCallBack)
                    }
                }
            }
        }
    }

    private fun initData() {
        mTime = TimeCount(60000, 1000, verifyCodeText)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTime?.cancel()
    }

    //获取验证码定时器
    class TimeCount(
        millisInFuture: Long,
        countDownInterval: Long,
        private val verifyCodeText: TextView
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            verifyCodeText.text = "获取验证码"
            verifyCodeText.setClickable(true)
        }

        override fun onTick(millisUntilFinished: Long) {
            verifyCodeText.setClickable(false)
            verifyCodeText.setText(
                String.format(
                    "%s",
                    millisUntilFinished.div(1000).toString() + "s"
                )
            )
        }
    }


    /*******************************************获取验证码回调 */
    private fun getVerifyCode() {
        verifyCallBack = object : CallBack<VerifyCodeResp?>() {
            override fun callBack(result: VerifyCodeResp?) {
                if (result == null) {
                    ToastUtils.showLong("验证码请求超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    mTime?.start()
                    ToastUtils.showLong("获取验证码成功")
                } else {
                    ToastUtils.showLong(msg)
                    mLoadingView.visibility = View.GONE
                    val userId = result.dataMap["userId"]
                    if (!TextUtils.isEmpty(userId)) {
                        activity?.finish()
                        ActivityUtils.startActivity(Intent(activity, LoginActivity::class.java))
                    }
                }
            }
        }
    }

    /*******************************************注册回调 */
    private fun appUserRegisterCallBack() {
        registerBack = object : CallBack<RegistResp?>() {
            override fun callBack(result: RegistResp?) {
                if (result == null) {
                    ToastUtils.showLong("注册请求超时")
                    mLoadingView.visibility = View.GONE
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    ToastUtils.showLong("注册成功")
                    val loginType = 2
                    authLoginCallBack()
                    if (GrpcAsyncTask.isFinish(loginTask)) {
                        loginTask = AuthApiService.getInstance()
                            .login(mPhone, passWord, loginType, loginCallBack)
                    }
                } else if (result.code == 308) {
                    result.userId
                } else {
                    ToastUtils.showLong(msg)
                    mLoadingView.visibility = View.GONE
                }
                //处理数据,刷新UI
            }

        }
    }

    /*******************************************登录回调 */
    fun authLoginCallBack() {
        loginCallBack = object : CallBack<LoginResp?>() {
            override fun callBack(result: LoginResp?) {
                if (result == null) {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong("注册后登陆超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    val token = result.token
                    val refreshToken = result.refreshToken
                    AppConfig.loginToken = token
                    AppConfig.refreshToken = refreshToken
                    //保存当前用户
                    SPUtils.getInstance().put(AppConfig.SH_USERNAME, mPhone)
                    SPUtils.getInstance().put(AppConfig.SH_PASSWORD, passWord)
                    bindAppCallBack()
                    if (GrpcAsyncTask.isFinish(bindTask)) {
                        bindTask =
                            UserCenterService.getInstance()
                                .bindAppUser(mPhone, mPhone, bindCallBack)
                    }
                } else {
                    ToastUtils.showLong(msg)
                    mLoadingView.visibility = View.GONE
                }
                //处理数据,刷新UI
            }
        }
    }

    /*******************************************绑定回调 */
    fun bindAppCallBack() {
        bindCallBack = object : CallBack<AppInfoResponse?>() {
            override fun callBack(result: AppInfoResponse?) {
                if (result == null) {
                    ToastUtils.showLong("登录后绑定超时")
                    mLoadingView.visibility = View.GONE
                    return
                }

                if (result.code === 100) {
                    mLoadingView.visibility = View.GONE
                    ActivityUtils.startActivity(Intent(activity, MainActivity::class.java))
                    //用户已经绑定
                } else if (result.code == 7003) {
                    mLoadingView.visibility = View.GONE
                    ActivityUtils.startActivity(Intent(activity, MainActivity::class.java))
                } else {
                    ToastUtils.showLong(result.msg)
                    mLoadingView.visibility = View.GONE
                }
                //处理数据,刷新UI
            }
        }
    }

    /*******************************************密码校验 */
    fun doExcute(password: String): Int {
        var kindOfCharacter = 0
        val digital = "[0-9]"
        val capital = "[A-Z]"
        val lowercase = "[a-z]"
        val spec = "[-~!@#$%^&*()_+{}|:<>?;',.]"
        /** 有大写字母或小写字母，即有字母  */
        for (i in 0 until password.length) {
            if (password.substring(i, i + 1).matches(capital.toRegex())
                || password.substring(i, i + 1).matches(lowercase.toRegex())
            ) {
                println(password.substring(i, i + 1))
                kindOfCharacter++
                break
            }
        }
        /** 有数字  */
        for (i in 0 until password.length) {
            if (password.substring(i, i + 1).matches(digital.toRegex())) {
                kindOfCharacter++
                break
            }
        }
        /** 有符号  */
        for (i in 0 until password.length) {
            if (password.substring(i, i + 1).matches(spec.toRegex())) {
                kindOfCharacter++
                break
            }
        }
        if (password.length < 6) {
            kindOfCharacter = 1
        }
        return kindOfCharacter
    }
}
