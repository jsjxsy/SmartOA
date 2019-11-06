package com.gx.smart.smartoa.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_main)
        val navView = nav_view as RadioGroup
        navView.setOnCheckedChangeListener(this)
    }



    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.id_tab_home ->
                Navigation.findNavController(
                    MainActivity@ this,
                    R.id.nav_host_fragment
                ).navigate(R.id.action_global_navigation_home)
            R.id.id_tab_open ->
                Navigation.findNavController(
                    MainActivity@ this,
                    R.id.nav_host_fragment
                ).navigate(R.id.action_global_openDoorFragment)
            R.id.id_tab_mine ->
                Navigation.findNavController(
                    MainActivity@ this,
                    R.id.nav_host_fragment
                ).navigate(R.id.action_global_mineFragment)
        }
    }

    /**
     * ******** mine Fragment ***********
     */
    fun mineSettings(view: View) {

    }

}
