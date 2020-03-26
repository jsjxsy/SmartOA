package com.gx.smart.module.login.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.gx.smart.module.login.LoginUtil
import com.gx.smart.module.login.R
import com.gx.smart.module.login.activity.LoginActivity
import com.gx.smart.module.login.base.BaseVerifyCodeFragment
import com.gx.smart.module.login.databinding.FragmentForgetPasswordBinding
import com.gx.smart.module.login.mvvm.viewmodel.ForgetPasswordViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_forget_password.*


class ForgetPasswordFragment :
    BaseVerifyCodeFragment<FragmentForgetPasswordBinding, ForgetPasswordViewModel>(),
    View.OnClickListener {

    private var mPhone: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
        arguments?.let {
            mPhone = it.getString(ARG_PHONE_NUMBER)
        }
    }

    override  fun onBindViewModelByDataBinding(dataBindingUtil: FragmentForgetPasswordBinding){
        dataBindingUtil.viewModel = viewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTimer(getVerifyCodeText)
        initContent()
        observer()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    private fun observer() {
        //开始倒计时
        viewModel.verifyCodeCallBackSuccess.observe(
            viewLifecycleOwner,
            Observer<Boolean> { getVerifyCode ->
                if (getVerifyCode) {
                    mTime?.start()
                }
            })

        viewModel.targetPage.observe(viewLifecycleOwner, Observer {
            when (it) {
                1 -> gotoLoginPage()
                else -> {
                    Logger.e("jump page argument is exception'")
                }
            }
        })
    }

    private fun gotoLoginPage() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ActivityUtils.startActivity(intent)
    }


    private fun initContent() {
        viewModel.phone.value = mPhone
        phone.isEnabled = false
    }


    companion object {
        const val ARG_PHONE_NUMBER = "phoneNumber"
    }

    override fun onBindViewModelFactory() = LoginUtil.getLoginFactory()
    override fun onBindLayout() = R.layout.fragment_forget_password
    override fun onBindViewModel(): Class<ForgetPasswordViewModel> = ForgetPasswordViewModel::class.java

}
