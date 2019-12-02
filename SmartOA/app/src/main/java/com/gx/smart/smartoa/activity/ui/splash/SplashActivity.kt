package com.gx.smart.smartoa.activity.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.smartoa.base.BaseActivity

class SplashActivity : BaseActivity() {

    //权限列表
    var permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.DISABLE_KEYGUARD
    )
    var mPermissionList: MutableList<String> = arrayListOf()
    private val mRequestCode: Int = 100 //权限请求码

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        requestPermission()
    }

    private fun requestPermission() { //判断哪些权限未授予
        mPermissionList.clear()
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permissions.get(i)
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionList.add(permissions.get(i))
            }
        }
        //申请权限
        if (mPermissionList.size > 0) { //有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode)
        } else { //说明权限都已经通过，可以做你想做的事情去
            window.decorView.postDelayed(
                {
                    startActivity(Intent(SplashActivity@ this, LoginActivity::class.java))

                },
                DELAY_TIME
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermissionDismiss = false //有权限没有通过
        if (mRequestCode == requestCode) {
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) { //重新申请
                ActivityCompat.requestPermissions(this, permissions, mRequestCode)
            } else {
                window.decorView.postDelayed(
                    {
                        startActivity(Intent(SplashActivity@ this, LoginActivity::class.java))

                    },
                    DELAY_TIME
                )
            }
        }
    }


    companion object {
        const val DELAY_TIME: Long = 1000 * 3
    }
}
