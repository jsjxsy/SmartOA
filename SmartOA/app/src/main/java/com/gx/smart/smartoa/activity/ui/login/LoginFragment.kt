package com.gx.smart.smartoa.activity.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.data.model.User
import com.gx.smart.smartoa.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.id_login_button -> login()
            R.id.id_forget_password_text_view ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            R.id.id_register_text_view ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
            R.id.loginType -> loginType()
            R.id.getVerifyCode -> getVerifyCode()

        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var loginFlag = LoginTypeEnum.PHONE

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
        // TODO: Use the ViewModel
        id_login_button.setOnClickListener(this)
        id_forget_password_text_view.setOnClickListener(this)
        id_register_text_view.setOnClickListener(this)
        loginType.setOnClickListener(this)
        getVerifyCode.setOnClickListener(this)
    }

    private fun loginType() {
        when (loginFlag) {
            LoginTypeEnum.PHONE -> {
                loginFlag = LoginTypeEnum.VERIFY_CODE
                loginType.text = getString(R.string.login_phone_verify)
                val leftDrawable =
                    resources.getDrawable(R.drawable.ic_login_password, activity?.theme)
                val rightDrawable =
                    resources.getDrawable(R.drawable.ic_login_password_state, activity?.theme)
                id_input_password_edit_text.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    leftDrawable,
                    null,
                    rightDrawable,
                    null
                )
                getVerifyCode.visibility = View.GONE

            }
            LoginTypeEnum.VERIFY_CODE -> {
                loginFlag = LoginTypeEnum.PHONE
                loginType.text = getString(R.string.login_phone_password)
                val leftDrawable =
                    resources.getDrawable(R.drawable.ic_login_password, activity?.theme)
                id_input_password_edit_text.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    leftDrawable,
                    null,
                    null,
                    null
                )
                getVerifyCode.visibility = View.VISIBLE
            }
        }


    }

    /**
     * 点击登陆
     */
    private fun login() {
        //request network
        //result
        //jump page
        if (user.phone.isNotEmpty() && user.password.isNotEmpty()) {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private fun getVerifyCode() {

    }

}
