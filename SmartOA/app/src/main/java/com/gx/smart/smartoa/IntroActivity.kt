package com.gx.smart.smartoa

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.gx.smart.smartoa.activity.ui.splash.SplashActivity


class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.TRANSPARENT
        }


        val sliderPage = SliderPage()
        sliderPage.imageDrawable = R.mipmap.guide1
        sliderPage.bgColor = Color.WHITE
        addSlide(AppIntroFragment.newInstance(sliderPage))

        val sliderPage2 = SliderPage()
        sliderPage2.bgColor = Color.WHITE
        sliderPage2.imageDrawable = R.mipmap.guide2
        addSlide(AppIntroFragment.newInstance(sliderPage2))

        val sliderPage3 = SliderPage()
        sliderPage3.bgColor = Color.WHITE
        sliderPage3.imageDrawable = R.mipmap.guide3
        addSlide(AppIntroFragment.newInstance(sliderPage3))

        // Hide Skip/Done button.
        setColorSkipButton(resources.getColor(R.color.font_color_style_six))
        setColorDoneText(resources.getColor(R.color.font_color_style_six))
        setIndicatorColor(resources.getColor(R.color.font_color_style_six), resources.getColor(R.color.font_color_style_five))
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
