package com.gx.smart.smartoa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.gx.smart.smartoa.activity.ui.splash.SplashActivity


class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT

        val sliderPage = SliderPage()
        sliderPage.title = "this is one"
        sliderPage.description = "this is two"
        sliderPage.imageDrawable = R.mipmap.home_banner_test
        sliderPage.bgColor = Color.BLACK
        addSlide(AppIntroFragment.newInstance(sliderPage))

        val sliderPage2 = SliderPage()
        sliderPage2.title = "this is one"
        sliderPage2.description = "this is two"
        sliderPage.bgColor = Color.BLACK
        sliderPage2.imageDrawable = R.mipmap.home_banner_test
        addSlide(AppIntroFragment.newInstance(sliderPage2))

        // Hide Skip/Done button.
        showSkipButton(false)
        isProgressButtonEnabled = false
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Do something when users tap on Skip button.
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Do something when users tap on Done button.
        finish()
        startActivity(Intent(this, SplashActivity::class.java))
    }

    override fun onSlideChanged(@Nullable oldFragment: Fragment?, @Nullable newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }
}
