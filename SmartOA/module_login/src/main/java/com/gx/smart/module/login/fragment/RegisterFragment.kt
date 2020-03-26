package com.gx.smart.module.login.fragment

//import kotlinx.android.synthetic.main.layout_common_title.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.gx.smart.module.login.LoginUtil
import com.gx.smart.module.login.R
import com.gx.smart.module.login.activity.LoginActivity
import com.gx.smart.module.login.base.BaseVerifyCodeFragment
import com.gx.smart.module.login.databinding.RegisterFragmentBinding
import com.gx.smart.module.login.mvvm.viewmodel.RegisterViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseVerifyCodeFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    private val viewModel by lazy { ViewModelProvider(this,LoginUtil.getLoginFactory())[RegisterViewModel::class.java] }
    private lateinit var dataBinding: RegisterFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         dataBinding = DataBindingUtil.inflate<RegisterFragmentBinding>(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        initTitle()
        initTimer(getVerifyCodeText)
        observer()
    }



    private fun initTitle() {
//        left_nav_image_view.visibility = View.VISIBLE
//        center_title?.let {
//            it.visibility = View.VISIBLE
//            it.text = getString(R.string.register_account)
//        }
//        left_nav_image_view.setOnClickListener(this)
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
                1 -> gotoLoginPage()
                2 -> mineCompanyActivity()
                else -> {
                    Logger.e("jump page argument is exception'")
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }


    private fun gotoLoginPage() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ActivityUtils.startActivity(intent)
    }

    private fun mineCompanyActivity() {
        ARouter.getInstance().build("/app/mine/company")
            .withString("fromLogin", "fromLogin")
            .navigation()
    }

}
