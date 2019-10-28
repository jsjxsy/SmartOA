package com.gx.smart.smartoa.activity.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.data.model.User
import com.gx.smart.smartoa.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment(), OnClickListener {
    override fun onClick(v: View) {
        when(v.id){
            R.id.id_login_button -> login()
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    var user = User("", "")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginFragmentBinding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment,
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
    }


    /**
     * 点击登陆
     */
    fun login() {
        //request network
        //result
        //jump page
        if (user.phone.isNotEmpty() && user.password.isNotEmpty()) {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

}
