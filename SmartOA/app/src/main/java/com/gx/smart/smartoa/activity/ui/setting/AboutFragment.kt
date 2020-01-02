package com.gx.smart.smartoa.activity.ui.setting


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils

import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.WebViewActivity
import com.gx.smart.smartoa.data.network.ApiConfig
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment(),View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.about)
        }
    }


    private fun initContent() {
        agreement.setOnClickListener(this)
        version.text = "当前版本 v" +AppUtils.getAppVersionName()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.agreement -> goWebView(ApiConfig.WEB_AGREEMENT_URL)
        }
    }


    private fun goWebView(url: String) {
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL, url)
        ActivityUtils.startActivity(intent)
    }

}
