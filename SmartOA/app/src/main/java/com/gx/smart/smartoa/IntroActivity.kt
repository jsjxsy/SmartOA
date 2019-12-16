package com.gx.smart.smartoa

import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage


class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val sliderPage = SliderPage()
        sliderPage.title = "this is one"
        sliderPage.description = "this is two"
        sliderPage.imageDrawable = R.mipmap.home_banner_test
        addSlide(AppIntroFragment.newInstance(sliderPage))

        val sliderPage2 = SliderPage()
        sliderPage2.title = "this is one"
        sliderPage2.description = "this is two"
        sliderPage2.imageDrawable = R.mipmap.home_banner_test
        addSlide(AppIntroFragment.newInstance(sliderPage2))

        // OPTIONAL METHODS
        // Override bar/separator color.
        // OPTIONAL METHODS
        // Override bar/separator color.
       // setBarColor(Color.parseColor("#3F51B5"))
        //setSeparatorColor(Color.parseColor("#2196F3"))

        // Hide Skip/Done button.
        // Hide Skip/Done button.
        showSkipButton(false)
        setProgressButtonEnabled(false)
    }
}
