package com.gx.smart.module.login.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.gx.smart.module.login.LoginUtil
import com.gx.smart.module.login.R
import com.gx.smart.module.login.base.BaseVerifyCodeFragment
import com.gx.smart.module.login.databinding.FragmentLoginBinding
import com.gx.smart.module.login.mvvm.viewmodel.LoginViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseVerifyCodeFragment(), OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.passwordState -> passwordState()
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            LoginUtil.getLoginFactory()
        ).get(
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
        viewModel.verifyCodeCallBackSuccess.observe(viewLifecycleOwner, Observer<Boolean> { getVerifyCode ->
            if (getVerifyCode) {
                mTime?.start()
            }
        })

        viewModel.targetPage.observe(viewLifecycleOwner, Observer {
            when (it) {
                1 -> mainActivity()
                2 -> mineCompanyActivity()
                else -> {
                    Logger.e("jump page argument is exception'")
                }
            }
        })

        viewModel.loginTypeFlag.observe(viewLifecycleOwner, Observer {
            passwordEdit.editableText.clear()
            loginTypeSwitch(it)
        })

        viewModel.passwordState.observe(viewLifecycleOwner, Observer {
            passwordState()
        })
    }

    private fun initContent() {
        viewModel.passwordState.value = false
        viewModel.setPhone()
    }

    /**
     * 密码可见不可见切换
     */
    private fun passwordState() {
        when (viewModel.passwordState.value) {
            //不可见变为可见
            true -> {
                passwordState.setImageResource(R.drawable.ic_login_password_state_visible)
                passwordEdit.inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                //设置密码为明文，并更改眼睛图标
                passwordEdit.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }

            //可见变为不可见
            false -> {
                passwordState.setImageResource(R.drawable.ic_login_password_state)
                passwordEdit.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordEdit.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
        val index = passwordEdit.text.length
        passwordEdit.setSelection(index)
    }

    /**
     * 登陆类型切换
     */
    private fun loginTypeSwitch(type: LoginViewModel.LoginTypeEnum) {
        when (type) {
            LoginViewModel.LoginTypeEnum.PHONE -> {
                loginType.text = getString(R.string.login_phone_password)
                passwordEdit.maxHeight = 12
                passwordEdit.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                getVerifyCodeText.visibility = View.GONE
                passwordState.visibility = View.VISIBLE
                passwordState.setImageResource(R.drawable.ic_login_password_state)
                //可见变为不可见
                passwordEdit.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            LoginViewModel.LoginTypeEnum.VERIFY_CODE -> {
                loginType.text = getString(R.string.login_phone_verify)
                passwordEdit.maxHeight = 6
                passwordEdit.inputType = InputType.TYPE_CLASS_NUMBER
                getVerifyCodeText.visibility = View.VISIBLE
                passwordState.visibility = View.GONE
                passwordEdit.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
        }

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
