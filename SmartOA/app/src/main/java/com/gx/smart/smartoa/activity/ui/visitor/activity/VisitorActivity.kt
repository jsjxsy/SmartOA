package com.gx.smart.smartoa.activity.ui.visitor.activity

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_visitor.*

class VisitorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor)
        val navHostFragment = visitorFragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_visitor)
        if (intent.hasExtra("toMineVisitorRecordFragment")) {
            graph.startDestination = R.id.mineVisitorRecordFragment
        } else {
            graph.startDestination = R.id.visitorFragment
        }
        navHostFragment.navController.graph = graph
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.visitorFragment).navigateUp()

}
