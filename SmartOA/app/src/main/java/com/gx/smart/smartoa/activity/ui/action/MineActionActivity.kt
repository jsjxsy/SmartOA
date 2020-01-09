package com.gx.smart.smartoa.activity.ui.action

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mine_action.*

class MineActionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_action)
        val navHostFragment = mineActionFragmentEnter as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_mine_action)
        if (intent.hasExtra(FROM_HOME) ||
            intent.hasExtra(FROM_MESSAGE)) {
            graph.startDestination = R.id.mineActionDetailFragment
        } else {
            graph.startDestination = R.id.mineActionFragment
        }
        navHostFragment.navController.graph = graph
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.mineActionFragmentEnter).navigateUp()

    companion object {
        const val FROM_HOME = "fromHome"
        const val FROM_MESSAGE = "fromMessage"
        const val FROM_MORE= "fromMore"
        const val FROM_MINE= "fromMine"
    }
}
