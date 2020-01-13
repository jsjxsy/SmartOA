package com.gx.smart.smartoa.activity.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.activity.ui.login.password.ForgetPasswordFragment
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.databinding.FragmentLoginBinding
import com.gx.smart.smartoa.utils.DataCheckUtil
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
            R.id.getVerifyCodeText -> viewModel.getVerifyCode()
            R.id.passwordState -> passwordState()

        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
    private var loginFlag = LoginTypeEnum.PHONE
    private var mTime: TimeCount? = null

    enum class LoginTypeEnum {
        PHONE, VERIFY_CODE
    }

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
        loginFragmentBinding.userModel = viewModel
        return loginFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initContent()
        initTimer()
        observer()

    }

    private fun observer() {
        viewModel.verifyCodeCallBackSuccess.observe(this, Observer<Boolean> { getVerifyCode ->
            if (getVerifyCode) {
                mTime?.start()
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (!isLoading) {
                //loadingView.visibility = View.GONE
            }
        })

        viewModel.targetPage.observe(this, Observer {
            when (it) {
                1 -> mainActivity()
                2 -> mineCompanyActivity()
            }
        })
    }

    fun initContent() {
        id_login_button.setOnClickListener(this)
        id_forget_password_text_view.setOnClickListener(this)
        id_register_text_view.setOnClickListener(this)
        loginType.setOnClickListener(this)
        getVerifyCodeText.setOnClickListener(this)
        passwordState.setOnClickListener(this)

        id_input_password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        id_input_password_edit_text.transformationMethod =
            PasswordTransformationMethod.getInstance()
        viewModel.setPhone()
        delete.setOnClickListener {
            id_input_phone_edit_text.editableText.clear()
        }
        id_input_phone_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                if (length > 0) {
                    delete.visibility = View.VISIBLE
                } else {
                    delete.visibility = View.GONE
                }
                id_input_phone_edit_text.setSelection(length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun loginType() {
        id_input_password_edit_text.editableText.clear()
        when (loginFlag) {
            LoginTypeEnum.PHONE -> {
                loginFlag = LoginTypeEnum.VERIFY_CODE
                loginType.text = getString(R.string.login_phone_verify)
                id_input_password_edit_text.maxHeight = 6
                id_input_password_edit_text.inputType = InputType.TYPE_CLASS_NUMBER
                getVerifyCodeText.visibility = View.VISIBLE
                passwordState.visibility = View.GONE
                id_input_password_edit_text.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            LoginTypeEnum.VERIFY_CODE -> {

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
        val index = id_input_password_edit_text.text.length
        id_input_password_edit_text.setSelection(index)

    }

    /**
     * 点击登陆
     */
    private fun login() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        val password: String = viewModel.password.value!!.trim()
        if (TextUtils.isEmpty(viewModel.phone.value)) {
            ToastUtils.showLong("手机号不能为空")
            return
        }
        if (viewModel.phone.value?.length != 11 || !DataCheckUtil.isMobile(viewModel.phone.value)) {
            ToastUtils.showLong("非法手机号")
            return
        }


        when (loginFlag) {
            LoginTypeEnum.VERIFY_CODE -> {
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong("验证码不能为空")
                    return
                }
                viewModel.login(3)
            }
            LoginTypeEnum.PHONE -> {

                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong("密码不能为空")
                    return
                }
                viewModel.login(2)
            }

        }

//        loadingView.visibility = View.VISIBLE
//        loadingView.setText("登录中")
//        loadingView.showLoading()
    }


    private fun initTimer() {
        mTime = TimeCount(60000, 1000, getVerifyCodeText)
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


    private fun mainActivity() {
        activity?.finish()
        ActivityUtils.startActivity(
            Intent(activity, MainActivity::class.java)
        )
    }

    private fun mineCompanyActivity() {
        val intent = Intent(
            context,
            MineCompanyActivity::class.java
        )
        intent.putExtra(MineCompanyActivity.FROM_LOGIN, MineCompanyActivity.FROM_LOGIN)
        ActivityUtils.startActivity(
            intent
        )
    }


}
