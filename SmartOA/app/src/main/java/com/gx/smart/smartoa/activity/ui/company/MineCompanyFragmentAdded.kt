package com.gx.smart.smartoa.activity.ui.company


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AppEmployeeService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.gx.wisestone.work.app.grpc.employee.EmployeeInfo
import kotlinx.android.synthetic.main.fragment_mine_company_added.*
import top.limuyang2.customldialog.IOSMsgDialog

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyFragmentAdded : Fragment(), View.OnClickListener {
    var statue: Int = 0
    lateinit var employeeInfo: EmployeeInfo
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.unbindCompany -> {
                tipCancelAction()
            }
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }

    }


    private fun tipCancelAction() {
        when (statue) {
            1 -> IOSMsgDialog.init(fragmentManager!!)
                .setTitle("解绑公司")
                .setMessage("您确定撤销企业申请?")
                .setNegativeButton("取消")
                .setPositiveButton("确定", View.OnClickListener {
                    cancelCompanyApply()
                }).show()
            2 -> IOSMsgDialog.init(fragmentManager!!)
                .setTitle("解绑公司")
                .setMessage("您确定解绑企业?")
                .setNegativeButton("取消")
                .setPositiveButton("确定", View.OnClickListener {
                    cancelCompanyBind()
                }).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_company_added, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initContent()
        unbindCompany.setOnClickListener(this)
    }


    private fun initContent() {
        if (employeeInfo.imageUrl.isNotBlank()) {
            Glide.with(this).load(employeeInfo.imageUrl).into(logoCompany)
        }
        companyName.text = employeeInfo.companyName
        companyDepartment.text = employeeInfo.departmentName
        verify.text = when (employeeInfo.status) {
            1 -> {
                statue = 1
                verify.setTextColor(context?.resources!!.getColor(R.color.font_color_style_eleven))
                "审核中"
            }
            2 -> {
                statue = 2
                verify.setTextColor(context?.resources!!.getColor(R.color.font_color_style_night))
                "已认证"
            }
            else -> {
                verify.setTextColor(context?.resources!!.getColor(R.color.font_color_style_ten))
                verify.setTextColor(Color.RED)
                "拒绝"
            }
        }
        Glide.with(this).load(employeeInfo.imageUrl)
            .placeholder(R.mipmap.default_image_small)
            .error(R.mipmap.default_image_small)
            .into(logoCompany)
    }

    private fun cancelCompanyBind() {
        loadingView.visibility = View.VISIBLE
        AppEmployeeService.getInstance()
            .cancelCompanyBind(
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if(!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("解绑公司超时!")
                            return
                        }
                        if (result.code == 100) {
                            ToastUtils.showLong("解绑公司成功!")
                            SPUtils.getInstance().remove(AppConfig.COMPANY_NAME)
                            SPUtils.getInstance().remove(AppConfig.EMPLOYEE_ID)
                            SPUtils.getInstance().remove(AppConfig.COMPANY_APPLY_STATUS)
                            activity?.onBackPressed()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }


    private fun cancelCompanyApply() {
        loadingView.visibility = View.VISIBLE
        AppEmployeeService.getInstance()
            .cancelCompanyApply(employeeInfo.applyId,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if(!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("解绑公司超时!")
                            return
                        }
                        if (result.code == 100) {
                            ToastUtils.showLong("解绑公司成功!")
                            SPUtils.getInstance().remove(AppConfig.COMPANY_NAME)
                            SPUtils.getInstance().remove(AppConfig.EMPLOYEE_ID)
                            SPUtils.getInstance().remove(AppConfig.COMPANY_APPLY_STATUS)
                            activity?.onBackPressed()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }

}
