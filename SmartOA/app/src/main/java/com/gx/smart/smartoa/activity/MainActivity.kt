package com.gx.smart.smartoa.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.RadioGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.home.HomeFragment
import com.gx.smart.smartoa.activity.ui.mine.MineFragment
import com.gx.smart.smartoa.activity.ui.open.OpenDoorFragment
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



    fun stateSetting() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        var childFragment = fragment.childFragmentManager.fragments[0]
        when (childFragment) {
            is HomeFragment -> id_tab_home.isChecked = true
            is OpenDoorFragment -> id_tab_open.isChecked = true
            is MineFragment -> id_tab_mine.isChecked = true
        }
    }

}
