package com.gx.smart.smartoa.activity.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.smartoa.base.BaseActivity

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        window.decorView.postDelayed(
            {
                startActivity(Intent(SplashActivity@ this, LoginActivity::class.java))

            },
            DELAY_TIME
        )
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        const val DELAY_TIME: Long = 1000 * 3
    }
}
