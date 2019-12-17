package com.gx.smart.smartoa.activity.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.login.password.ForgetPasswordFragment
import com.gx.smart.smartoa.activity.ui.splash.SplashActivity.Companion.DELAY_TIME
import com.gx.smart.smartoa.data.model.User
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.AuthApiService
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.smart.smartoa.databinding.FragmentLoginBinding
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.smart.smartoa.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.LoginResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.id_login_button -> login()
            R.id.id_forget_password_text_view -> {
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

            R.id.id_register_text_view ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
            R.id.loginType -> loginType()
            R.id.getVerifyCodeText -> getVerifyCode()
            R.id.passwordState -> passwordState()

        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var loginFlag = LoginTypeEnum.PHONE
    private var mTime: TimeCount? = null
    private lateinit var verifyCodeText: TextView
    private lateinit var mLoadingView: LoadingView
    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null

    private var loginTask: GrpcAsyncTask<String, Void, LoginResp>? = null
    private var loginCallBack: CallBack<LoginResp?>? = null

    private var bindTask: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var bindCallBack: CallBack<AppInfoResponse?>? = null

    private var mPhone: String? = null

    enum class LoginTypeEnum {
        PHONE, VERIFY_CODE
    }

    var user = User("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginFragmentBinding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        loginFragmentBinding.userModel = user
        return loginFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        initContent()
        initTimer()
    }

    private fun initContent() {
        id_login_button.setOnClickListener(this)
        id_forget_password_text_view.setOnClickListener(this)
        id_register_text_view.setOnClickListener(this)
        loginType.setOnClickListener(this)
        getVerifyCodeText.setOnClickListener(this)
        passwordState.setOnClickListener(this)
        mLoadingView = loadingView

        id_input_password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        id_input_password_edit_text.transformationMethod =
            PasswordTransformationMethod.getInstance()
        val userName = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
        id_input_phone_edit_text.setText(userName)
        id_input_phone_edit_text.setSelection(userName.length)
    }

    private fun loginType() {
        when (loginFlag) {
            LoginTypeEnum.PHONE -> {
                loginFlag = LoginTypeEnum.VERIFY_CODE
                id_input_password_edit_text.editableText.clear()
                loginType.text = getString(R.string.login_phone_verify)
                id_input_password_edit_text.maxHeight = 6
                id_input_password_edit_text.inputType = InputType.TYPE_CLASS_NUMBER
                getVerifyCodeText.visibility = View.VISIBLE
                passwordState.visibility = View.GONE
                id_input_password_edit_text.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            LoginTypeEnum.VERIFY_CODE -> {
                id_input_password_edit_text.editableText.clear()
                loginFlag = LoginTypeEnum.PHONE
                loginType.text = getString(R.string.login_phone_password)
                id_input_password_edit_text.maxHeight = 12
                id_input_password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                getVerifyCodeText.visibility = View.GONE
                passwordState.visibility = View.VISIBLE
                passwordState.setImageResource(R.drawable.ic_login_password_state)
                //可见变为不可见
                id_input_password_edit_text.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }


    }

    private fun passwordState() {
        val index = id_input_password_edit_text.text.length
        id_input_password_edit_text.setSelection(index)
        when (id_input_password_edit_text.inputType) {
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                passwordState.setImageResource(R.drawable.ic_login_password_state_visible)
                id_input_password_edit_text.inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                //不可见变为可见
                //设置密码为明文，并更改眼睛图标
                id_input_password_edit_text.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }

            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> {
                passwordState.setImageResource(R.drawable.ic_login_password_state)
                id_input_password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                //可见变为不可见
                id_input_password_edit_text.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

    }

    /**
     * 点击登陆
     */
    private fun login() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        mPhone = id_input_phone_edit_text.text.toString()
        when (loginFlag) {
            LoginTypeEnum.VERIFY_CODE -> {
                val identityCode: String = id_input_password_edit_text.text.toString().trim()
                if (TextUtils.isEmpty(mPhone)) {
                    ToastUtils.showLong("手机号不能为空")
                } else if (mPhone!!.length != 11 || !DataCheckUtil.isMobile(mPhone)) {
                    ToastUtils.showLong("非法手机号")
                } else if (TextUtils.isEmpty(identityCode)) {
                    ToastUtils.showLong("验证码不能为空")
                } else { //手机号验证码登录
                    val loginType = 3
                    loginResponseCallBack(false, mPhone, null)
                    if (GrpcAsyncTask.isFinish(loginTask)) {
                        loginTask = AuthApiService.getInstance()
                            .login(mPhone, identityCode, loginType, loginCallBack)
                    }
                }
            }
            LoginTypeEnum.PHONE -> {
                val password: String = id_input_password_edit_text.text.toString().trim()
                if (TextUtils.isEmpty(mPhone)) {
                    ToastUtils.showLong("帐号名不能为空")
                } else if (mPhone!!.length < 10) {
                    ToastUtils.showLong("帐号名格式错误，长度不低于10位")
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong("密码不能为空")
                } else {
                    mLoadingView.visibility = View.VISIBLE
                    mLoadingView.setOnClickListener(null)
                    mLoadingView.setText("登录中")
                    mLoadingView.showLoading()
                    mLoadingView.postDelayed(
                        { mLoadingView.visibility = View.GONE },
                        DELAY_TIME
                    )
                    //手机号密码登录
                    val loginType = 2
                    loginResponseCallBack(true, mPhone, password)
                    if (GrpcAsyncTask.isFinish(loginTask)) {
                        loginTask = AuthApiService.getInstance()
                            .login(mPhone, password, loginType, loginCallBack)
                    }
                }
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
        mPhone = id_input_phone_edit_text.text.toString()
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
    private fun loginResponseCallBack(
        isPassWord: Boolean,
        phone: String?,
        password: String?
    ) {
        loginCallBack = object : CallBack<LoginResp?>() {
            override fun callBack(result: LoginResp?) {
                if (result == null) {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong("登录超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    AppConfig.loginToken = result.token
                    AppConfig.refreshToken = result.refreshToken
                    //保存当前用户
                    if (isPassWord) { //保存当前用户
                        SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone)
                        SPUtils.getInstance().put(AppConfig.SH_PASSWORD, password)
                    } else {
                        SPUtils.getInstance().put(AppConfig.SH_USER_ACCOUNT, phone)
                    }
                    bindAppCallBack()
                    if (GrpcAsyncTask.isFinish(bindTask)) {
                        bindTask =
                            UserCenterService.getInstance()
                                .bindAppUser(phone, phone, bindCallBack)
                    }
                } else {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong(msg)
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
                    mLoadingView.visibility = View.GONE
                    return
                }

                when {
                    result.getCode() === 100 -> {
                        myCompany()
                        //用户已经绑定
                    }
                    result.code == 7003 -> {
                        myCompany()
                    }
                    else -> {
                        ToastUtils.showLong(result.msg)
                        mLoadingView.visibility = View.GONE
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
                        mLoadingView?.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("查询我的企业超时!")
                            return
                        }
                        if (result?.code == 100) {
                            val employeeList = result.employeeInfoList
                            if (employeeList.isNotEmpty()) {
                                val employeeInfo = employeeList[0]
                                AppConfig.employeeId = employeeInfo.employeeId
                                AppConfig.currentSysTenantNo = employeeInfo.tenantNo
                            }
                            mainActivity()
                        } else {
                            ToastUtils.showLong(result.msg)
                            mLoadingView.visibility = View.GONE
                        }
                    }

                })
    }


    private fun mainActivity() {
        activity?.finish()
        ActivityUtils.startActivity(
            Intent(activity, MainActivity::class.java)
        )
    }


}
