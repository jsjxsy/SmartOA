package com.gx.smart.smartoa.activity.ui.login

import android.os.Bundle
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.loginFragmentEnter).navigateUp()

}
