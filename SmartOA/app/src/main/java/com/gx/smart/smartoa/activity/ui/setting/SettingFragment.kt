package com.gx.smart.smartoa.activity.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.smartoa.activity.ui.setting.utils.DataCleanManager
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.setting_fragment.*
import top.limuyang2.customldialog.IOSMsgDialog

class SettingFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private var appLogoutCallBack: CallBack<CommonResponse>? = null
    private var appLogoutTask: GrpcAsyncTask<String, Void, CommonResponse>? = null
    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        initTitle()
        initContent()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.mine_settings)
        }
    }

    private fun initContent() {
        val totalSize = DataCleanManager.getTotalSize(activity)
        cacheSize.text = DataCleanManager.getTotalCacheSize(activity)
        headLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_notDisturbFragment)
        }

        contactLayout.setOnClickListener {
            DataCleanManager.clearAllCache(activity)
            val newTotalSize = DataCleanManager.getTotalSize(activity)
            val deleteSize =
                DataCleanManager.getFormatSize(totalSize.toDouble() - newTotalSize.toDouble())
            ToastUtils.showLong(getString(R.string.cleared_cache) + deleteSize)
            cacheSize.text = DataCleanManager.getFormatSize(newTotalSize.toDouble())
        }

        suggestionLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_suggestionFragment)
        }

        accountLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_accountManagerFragment)
        }

        aboutLayout.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_settingFragment_to_aboutFragment)
        }

        logout.setOnClickListener {

            IOSMsgDialog.init(fragmentManager!!)
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setNegativeButton("取消", View.OnClickListener {

                })
                .setPositiveButton("确定", View.OnClickListener {
                    logout()
                }).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }

    }

    private fun logout() {
        appLogout()
        if (GrpcAsyncTask.isFinish(appLogoutTask)) {
            appLogoutTask = UserCenterService.getInstance().appLogout(appLogoutCallBack)
        }
    }

    //验证登录手机号回调
    private fun appLogout() {
        loadingView.visibility = View.VISIBLE
        appLogoutCallBack = object : CallBack<CommonResponse>() {
            override fun callBack(result: CommonResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                loadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("退出登录失败")
                    return
                }
                if (result.code == 100) {
                    clearCache()
                    //跳转登录界面
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtils.startActivity(intent)
                    ToastUtils.showLong("退出登录成功")
                    activity?.finish()
                } else {
                    ToastUtils.showLong(result.msg)
                }
            }
        }
    }


    fun clearCache() {
        SPUtils.getInstance()
            .remove(AppConfig.EMPLOYEE_ID)
        SPUtils.getInstance()
            .remove(AppConfig.COMPANY_SYS_TENANT_NO)
        SPUtils.getInstance()
            .remove(AppConfig.SMART_HOME_SN)
        SPUtils.getInstance()
            .remove(AppConfig.ROOM_ID)
        SPUtils.getInstance()
            .remove(AppConfig.COMPANY_PLACE_NAME)
        SPUtils.getInstance()
            .remove(AppConfig.COMPANY_NAME)
        SPUtils.getInstance()
            .remove(AppConfig.SH_USER_REAL_NAME)
        SPUtils.getInstance()
            .remove(AppConfig.SH_PASSWORD)
        SPUtils.getInstance()
            .remove(AppConfig.LOGIN_TOKEN)
        SPUtils.getInstance()
            .remove(AppConfig.USER_ID)
    }

}
