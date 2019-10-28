package com.gx.smart.smartoa.activity

import android.content.Intent
import android.os.Bundle
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        window.decorView.postDelayed(
            {
                startActivity(Intent(SplashActivity@ this, LoginActivity::class.java))

            },
            DELAY_TIME
        )
    }

    companion object {
        const val DELAY_TIME: Long = 1000 * 3
    }
}
