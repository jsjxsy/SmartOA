package com.gx.smart.repair

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.lib.base.BaseActivity

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
