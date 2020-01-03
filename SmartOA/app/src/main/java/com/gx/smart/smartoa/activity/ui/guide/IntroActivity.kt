package com.gx.smart.smartoa.activity.ui.guide

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.github.paolorotolo.appintro.AppIntro


class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.TRANSPARENT
        }

        addSlide(FirstFragment.newInstance())
        addSlide(SecondFragment.newInstance())
        addSlide(ThirdFragment.newInstance())
        setSeparatorColor(Color.WHITE)
        showSkipButton(false)
        isProgressButtonEnabled = false
    }

}
