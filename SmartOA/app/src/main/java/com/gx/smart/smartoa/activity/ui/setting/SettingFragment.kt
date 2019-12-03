package com.gx.smart.smartoa.activity.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.setting_fragment.*

class SettingFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
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
            it.text = getString(R.string.mine_settings)
        }
    }

    private fun initContent() {
        notDisturbLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_notDisturbFragment)
        }

        cacheLayout.setOnClickListener {

        }

        suggestionLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_suggestionFragment)
        }

        aboutLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_aboutFragment)
        }

        logout.setOnClickListener {
            ActivityUtils.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    override fun onClick(v: View?) {


    }


}
