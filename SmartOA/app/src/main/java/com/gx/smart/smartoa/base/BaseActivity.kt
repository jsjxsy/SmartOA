package com.gx.smart.smartoa.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.widget.LoadingDialog
import kotlinx.android.synthetic.main.layout_common_title.*
import top.limuyang2.ldialog.LDialog

/**
 *@author xiaosy
 *@create 2019-10-22
 *@Describe 基础类
 **/
open class BaseActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var loadingDialog: LDialog
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // version 5.0 after
        if (Build.VERSION.SDK_INT > 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE

        }
        left_nav_image_view?.setOnClickListener(this)
    }


    fun showLoadingDialog() {
        loadingDialog = LoadingDialog(supportFragmentManager)
            .showLoading()
            .setCancelableOutside(false)
            .show()

    }

    fun showLoadingDialog(text: String) {
        loadingDialog = LoadingDialog(supportFragmentManager)
            .setText(text)
            .showLoading()
            .setCancelableOutside(false)
            .show()

    }


    fun dismissLoadingDialog() {
        loadingDialog.dismiss()
    }

}