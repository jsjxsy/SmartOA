package com.gx.smart.smartoa.activity.ui.company

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.base.BaseActivity

class MineCompanyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_company)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.mineCompanyFragmentEnter) as NavHostFragment
        val mineCompanyEmployeesFragment =
            fragment.childFragmentManager.fragments[0] as? MineCompanyEmployeesFragment
        mineCompanyEmployeesFragment?.onActivityResult(requestCode, resultCode, data)

    }

}
