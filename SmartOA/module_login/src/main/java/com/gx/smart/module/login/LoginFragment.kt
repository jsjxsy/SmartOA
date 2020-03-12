package com.gx.smart.module.login

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.common.AppConfig
import com.gx.smart.module.login.base.BaseVerifyCodeFragment
import com.gx.smart.module.login.databinding.FragmentLoginBinding
import com.gx.smart.module.login.mvvm.viewmodel.LoginViewModel
import com.gx.smart.module.login.password.ForgetPasswordFragment
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseVerifyCodeFragment(), OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
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

            R.id.getVerifyCodeText -> viewModel.getVerifyCode()
            R.id.passwordState -> passwordState()

        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, LoginUtil.getLoginFactory()).get(
            LoginViewModel::class.java
        )
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
        loginFragmentBinding.lifecycleOwner = this
        loginFragmentBinding.userModel = viewModel
        return loginFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initContent()
        initTimer(getVerifyCodeText)
        observer()

    }

    private fun observer() {
        //开始倒计时
        viewModel.verifyCodeCallBackSuccess.observe(this, Observer<Boolean> { getVerifyCode ->
            if (getVerifyCode) {
                mTime?.start()
            }
        })

        viewModel.targetPage.observe(this, Observer {
            when (it) {
                1 -> mainActivity()
                2 -> mineCompanyActivity()
                else -> {
                    Logger.e("jump page argument is exception'")
                }
            }
        })

        viewModel.loginFlag.observe(this, Observer {
            id_input_password_edit_text.editableText.clear()
            when (it) {
                LoginViewModel.LoginTypeEnum.PHONE -> {
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
                LoginViewModel.LoginTypeEnum.VERIFY_CODE -> {
                    loginType.text = getString(R.string.login_phone_verify)
                    id_input_password_edit_text.maxHeight = 6
                    id_input_password_edit_text.inputType = InputType.TYPE_CLASS_NUMBER
                    getVerifyCodeText.visibility = View.VISIBLE
                    passwordState.visibility = View.GONE
                    id_input_password_edit_text.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }
                else -> {
                    Logger.e("login type argument is exception'")
                }
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
        id_input_phone_edit_text.setOnFocusChangeListener { _, b ->
            if (b) {
                id_input_phone_edit_text.setSelection(
                    viewModel.phone.value?.length ?: 0
                )
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


    private fun mainActivity() {
        activity?.finish()
        ARouter.getInstance().build("/app/main").navigation()
    }

    private fun mineCompanyActivity() {
        ARouter.getInstance().build("/app/mine/company")
            .withString("fromLogin", "fromLogin")
            .navigation()
    }


}
