package com.gx.smart.smartoa.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity
import com.gx.smart.smartoa.data.model.User
//import com.gx.smart.smartoa.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
//    private val binding by lazy {
//        DataBindingUtil.setContentView<ActivityLoginBinding>(
//            this,
//            R.layout.activity_login
//        )
//    }
//    var user = User("", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_login)
//        binding.userModel = user
    }

    /**
     * 点击登陆
     */
    fun login(view: View) {
        //request network
        //result
        //jump page
//        if (user.phone.isNotEmpty() && user.password.isNotEmpty()) {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
    }
}
