package com.gx.smart.smartoa.activity.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.data.model.User
import com.gx.smart.smartoa.databinding.ActivityLoginBinding

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(
            activity as Activity,
            R.layout.activity_login
        )
    }
    var user = User("", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.userModel = user
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }


    /**
     * 点击登陆
     */
    fun login(view: View) {
        //request network
        //result
        //jump page
        if (user.phone.isNotEmpty() && user.password.isNotEmpty()) {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

}
