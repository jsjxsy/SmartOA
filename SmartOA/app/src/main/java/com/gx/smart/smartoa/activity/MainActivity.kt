package com.gx.smart.smartoa.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ActivityUtils
import com.gx.smart.eventbus.EventBusMessageConstant
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.company.MineCompanyActivity
import com.gx.smart.smartoa.activity.ui.home.fragment.HomeFragment
import com.gx.smart.smartoa.activity.ui.mine.MineFragment
import com.gx.smart.smartoa.activity.ui.open.fragment.OpenDoorFragment
import com.gx.smart.lib.base.BaseActivity
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_main.*
import top.limuyang2.customldialog.IOSMsgDialog

@Route(path = "/app/main")
class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_main)
        val navView = nav_view as RadioGroup
        navView.setOnCheckedChangeListener(this)
        initEventBus()
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.id_tab_home ->
                Navigation.findNavController(
                    this,
                    R.id.navHostFragmentEnter
                ).navigate(R.id.action_global_navigation_home)
            R.id.id_tab_open ->
                Navigation.findNavController(
                    this,
                    R.id.navHostFragmentEnter
                ).navigate(R.id.action_global_openDoorFragment)
            R.id.id_tab_mine ->
                Navigation.findNavController(
                    this,
                    R.id.navHostFragmentEnter
                ).navigate(R.id.action_global_mineFragment)
        }
    }


    fun stateSetting() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentEnter) as NavHostFragment
        var childFragment = fragment.childFragmentManager.fragments[0]
        when (childFragment) {
            is HomeFragment -> id_tab_home.isChecked = true
            is OpenDoorFragment -> id_tab_open.isChecked = true
            is MineFragment -> id_tab_mine.isChecked = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentEnter) as NavHostFragment
        val homeFragment =
            fragment.childFragmentManager.fragments[0] as? HomeFragment
        homeFragment?.onActivityResult(requestCode, resultCode, data)

    }

    var dialog: IOSMsgDialog? = null
    private fun initEventBus() {

        LiveEventBus.get(EventBusMessageConstant.COMPANY_APPLY_STATUS_KEY, Int::class.java)
            .observe(this, Observer {
                val fragmentActivity = ActivityUtils.getTopActivity() as FragmentActivity
                if (dialog != null && dialog!!.isVisible) {
                    dialog!!.dismiss()
                }
                when (it) {
                    1 -> dialog = IOSMsgDialog.init(fragmentActivity.supportFragmentManager)
                        .setTitle("加入企业")
                        .setMessage("您申请的企业在审核中，请耐心等待")
                        .setPositiveButton("确定").show()

                    3 -> dialog = IOSMsgDialog.init(fragmentActivity.supportFragmentManager)
                        .setTitle("加入企业")
                        .setMessage("您还未入驻任何企业，请先进行企业身份认证")
                        .setPositiveButton("马上认证", View.OnClickListener {
                            ActivityUtils.startActivity(
                                Intent(
                                    ActivityUtils.getTopActivity(),
                                    MineCompanyActivity::class.java
                                )
                            )
                        }).show()
                }
            })

    }
}
