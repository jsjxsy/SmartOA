package com.gx.smart.smartoa.activity

import android.graphics.Color
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_login)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.loginFragment).navigateUp()
}
