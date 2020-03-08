package com.gx.smart.module.login

import android.os.Bundle
import androidx.navigation.Navigation
import android.view.WindowManager
import com.gx.smart.lib.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //让布局向上移来显示软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_login)
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.loginFragmentEnter).navigateUp()

}
