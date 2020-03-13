package com.gx.smart.smartoa.activity.ui.login.password

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
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.smart.lib.http.lib.utils.AuthUtils
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.smart.lib.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.UserModifyResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import kotlinx.android.synthetic.main.forget_password_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class ForgetPasswordFragment : Fragment(), View.OnClickListener {

    private var mTime: TimeCount? = null
    private lateinit var verifyCodeText: TextView
    private lateinit var mLoadingView: LoadingView
    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null

    private var userModifyPassWordTask: GrpcAsyncTask<String, Void, UserModifyResp>? = null
    private var userModifyPassWordTaskCallBack: CallBack<UserModifyResp>? = null

    private var loginTask: GrpcAsyncTask<String, Void, LoginResp>? = null
    private var loginCallBack: CallBack<LoginResp?>? = null
    private lateinit var viewModel: ForgetPasswordViewModel
    private var mPhone: String? = null
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
        arguments?.let {
            mPhone = it.getString(ARG_PHONE_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forget_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgetPasswordViewModel::class.java)
        initTitle()
        initContent()
        initTimer()
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.forget_password)
        }
        left_nav_image_view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }


    private fun initContent() {
        getVerifyCodeText.setOnClickListener {
            getVerifyCode()
        }
        mLoadingView = loadingView
        phone.setText(mPhone)
        phone.isEnabled = false
        modify.setOnClickListener {
            modify()
        }
    }


    /**
     * 点击登陆
     */
    private fun modify() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }

        val identityCode: String = verifyCode.text.toString().trim()
        password = newPassword.text.toString().trim()
        val confirmPassword: String = confirmNewPassword.text.toString().trim()

        if (TextUtils.isEmpty(mPhone)) {
            ToastUtils.showLong("手机号不能为空")
        } else if (mPhone!!.length != 11 || !DataCheckUtil.isMobile(mPhone)) {
            ToastUtils.showLong("非法手机号")
        } else if (TextUtils.isEmpty(identityCode)) {
            ToastUtils.showLong("验证码不能为空")
        } else if (TextUtils.isEmpty(password)) {
            ToastUtils.showLong("密码不能为空")
        } else if (doExcute(password) < 2) {
            ToastUtils.showLong("密码过于简单")
        } else if (password.length < 8) {
            ToastUtils.showLong("密码长度不得小于8位")
        } else if (password.length > 16) {
            ToastUtils.showLong("密码长度不得大于16位")
        } else if (TextUtils.isEmpty(confirmPassword)) {
            ToastUtils.showLong("确认密码不能为空")
        } else if (password != confirmPassword) {
            ToastUtils.showLong("确认密码与密码不一致")
        } else {
            //手机号验证码登录
            val loginType = 3
            mLoadingView.visibility = View.VISIBLE
            loginResponseCallBack()
            if (GrpcAsyncTask.isFinish(loginTask)) {
                loginTask = AuthApiService.getInstance()
                    .login(mPhone, identityCode, loginType, loginCallBack)
            }
        }

    }


    private fun initTimer() {
        verifyCodeText = getVerifyCodeText
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
            verifyCodeText.isClickable = true
        }

        override fun onTick(millisUntilFinished: Long) {
            verifyCodeText.isClickable = false
            verifyCodeText.text = String.format(
                "%s",
                millisUntilFinished.div(1000).toString() + "s"
            )
        }
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
                    mTime?.start()
                    ToastUtils.showLong("获取验证码成功")
                } else {
                    ToastUtils.showLong(msg)
                    mLoadingView.visibility = View.GONE
                }
            }
        }
    }

    private fun getVerifyCode() {
        mPhone = phone.text.toString()
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        if (TextUtils.isEmpty(mPhone)) {
            ToastUtils.showLong("手机号不能为空")
        } else if (mPhone?.length != 11 || !DataCheckUtil.isMobile(mPhone)) {
            ToastUtils.showLong("非法手机号")
        } else { //获取登录验证码
            val targetType = 1
            val purpose = 1
            verifyCodeCallBack()
            if (GrpcAsyncTask.isFinish(verifyTask)) {
                verifyTask =
                    AuthApiService.getInstance()
                        .verifyCode(mPhone, targetType, purpose, verifyCallBack)
            }
        }
    }


    /*******************************************登录回调 */
    private fun loginResponseCallBack() {
        loginCallBack = object : CallBack<LoginResp?>() {
            override fun callBack(result: LoginResp?) {

                if (result == null) {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong("登录超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    SPUtils.getInstance().put(AppConfig.LOGIN_TOKEN, result.token)
                    modifyPassword()
                } else {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong(msg)
                }
            }
        }
    }


    private fun modifyPassword() {
        appModifyPassword()
        val token =  SPUtils.getInstance().getString(AppConfig.LOGIN_TOKEN)
        val holder = AuthUtils.parseJwtHolder(token)
        val userId = holder.subject
        if (GrpcAsyncTask.isFinish(userModifyPassWordTask)) {
            userModifyPassWordTask = AuthApiService.getInstance()
                .userModifyPassWord(password, userId, token, userModifyPassWordTaskCallBack)
        }
    }

    /*******************************************修改密码回调 */
    private fun appModifyPassword() {
        userModifyPassWordTaskCallBack = object : CallBack<UserModifyResp>() {
            override fun callBack(result: UserModifyResp?) {
                if(!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                mLoadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("修改密码超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result?.code == 100) {
                    ToastUtils.showLong("修改密码成功")
                    activity?.finish()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtils.startActivity(intent)
                } else {
                    ToastUtils.showLong(msg)
                }
            }
        }
    }


    companion object {
        const val ARG_PHONE_NUMBER = "phoneNumber"
    }


    /*******************************************密码校验 */
    private fun doExcute(password: String): Int {
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
