package com.gx.smart.smartoa.activity.ui.company

import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mine_company.*

class MineCompanyActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_company)
        val navHostFragment = mineCompanyFragmentEnter as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_mine_company)
        if (intent.hasExtra(FROM_SPLASH) ||
            intent.hasExtra(FROM_HOME) ||
            intent.hasExtra(FROM_LOGIN)
        ) {
            graph.startDestination = R.id.mineCompanySelectAreaFragment
        } else {
            graph.startDestination = R.id.mineCompanyFragment
        }
        navHostFragment.navController.graph = graph
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.mineCompanyFragmentEnter).navigateUp()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.mineCompanyFragmentEnter) as NavHostFragment
        val mineCompanyEmployeesFragment =
            fragment.childFragmentManager.fragments[0] as? MineCompanyEmployeesFragment
        mineCompanyEmployeesFragment?.onActivityResult(requestCode, resultCode, data)

    }

    companion object {
        const val FROM_SPLASH = "fromSplash"
        const val FROM_HOME = "fromHome"
        const val FROM_LOGIN = "fromLogin"
    }

}
