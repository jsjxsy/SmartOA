package com.gx.smart.repair.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.gx.smart.arouter.ARouterConstants
import com.gx.smart.lib.base.BaseActivity
import com.gx.smart.repair.R
import com.gx.smart.repair.fragment.RepairFragment

@Route(path = ARouterConstants.REPAIR_PAGE)
class RepairActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repair)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.repairFragmentEnter) as NavHostFragment
        val repairFragment =
            fragment.childFragmentManager.fragments[0] as? RepairFragment
        repairFragment?.onActivityResult(requestCode, resultCode, data)

    }
}
