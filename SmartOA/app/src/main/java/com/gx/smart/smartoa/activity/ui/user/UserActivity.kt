package com.gx.smart.smartoa.activity.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseActivity

class UserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.mineUserInfoFragmentEnter) as NavHostFragment
        val mineUserInfoFragment =
            fragment.childFragmentManager.fragments[0] as? MineUserInfoFragment
        mineUserInfoFragment?.onActivityResult(requestCode, resultCode, data)

    }
}
