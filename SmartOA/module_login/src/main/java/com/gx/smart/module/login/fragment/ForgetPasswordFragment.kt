package com.gx.smart.module.login.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.gx.smart.module.login.LoginUtil
import com.gx.smart.module.login.R
import com.gx.smart.module.login.activity.LoginActivity
import com.gx.smart.module.login.base.BaseVerifyCodeFragment
import com.gx.smart.module.login.databinding.ForgetPasswordFragmentBinding
import com.gx.smart.module.login.mvvm.viewmodel.ForgetPasswordViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_forget_password.*
//import kotlinx.android.synthetic.main.layout_common_title.*

class ForgetPasswordFragment : BaseVerifyCodeFragment(), View.OnClickListener {

    private var mPhone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
        arguments?.let {
            mPhone = it.getString(ARG_PHONE_NUMBER)
        }
    }

    private val viewModel by lazy { ViewModelProvider(this, LoginUtil.getLoginFactory()).get(ForgetPasswordViewModel::class.java) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBindingUtil = DataBindingUtil.inflate<ForgetPasswordFragmentBinding>(
            inflater,
            R.layout.fragment_forget_password,
            container,
            false
        )
        dataBindingUtil.viewModel = viewModel
        dataBindingUtil.lifecycleOwner = this
        return dataBindingUtil.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        initTitle()
        initTimer(getVerifyCodeText)
        initContent()
        observer()
    }

//    private fun initTitle() {
//        left_nav_image_view.visibility = View.VISIBLE
//        center_title?.let {
//            it.visibility = View.VISIBLE
//            it.text = getString(R.string.forget_password)
//        }
//        left_nav_image_view.setOnClickListener(this)
//    }

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


}
