package com.gx.smart.smartoa.activity.ui.guide

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.github.paolorotolo.appintro.AppIntro


class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar(false)
        addSlide(FirstFragment.newInstance())
        addSlide(SecondFragment.newInstance())
        addSlide(ThirdFragment.newInstance())
        setSeparatorColor(Color.WHITE)
        showSkipButton(false)
        isProgressButtonEnabled = false
    }

}
