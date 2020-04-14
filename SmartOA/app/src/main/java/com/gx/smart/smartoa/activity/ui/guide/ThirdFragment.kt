package com.gx.smart.smartoa.activity.ui.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import com.blankj.utilcode.util.ActivityUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        start.setOnClickListener {
            activity?.finish()
            ActivityUtils.startActivity(Intent(context, SplashActivity::class.java))
        }
    }



    companion object {
        @JvmStatic
        fun newInstance() = ThirdFragment()
    }
}
