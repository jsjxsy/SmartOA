package com.gx.smart.smartoa.activity.ui.company


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.gx.wisestone.work.app.grpc.employee.EmployeeInfo
import kotlinx.android.synthetic.main.fragment_mine_company_added.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyFragmentAdded : Fragment(), View.OnClickListener {
     var statue:Int = 0
    lateinit var employeeInfo: EmployeeInfo
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.unbindCompany -> {
                when(statue){
                    1 -> cancelCompanyBind()
                    2 -> cancelCompanyApply()
                }
            }
            R.id.left_nav_image_view -> activity?.onBackPressed()
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
        AppConfig.employeeId = employeeInfo.employeeId
        AppConfig.currentSysTenantNo = employeeInfo.tenantNo
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
        Glide.with(this).load(employeeInfo.imageUrl).into(logoCompany)
    }

    private fun cancelCompanyBind() {
        loadingView.visibility = View.VISIBLE
        AppEmployeeService.getInstance()
            .cancelCompanyBind(
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("解绑公司超时!")
                            return
                        }
                        if (result?.code == 100) {
                            ToastUtils.showLong("解绑公司成功!")
                            SPUtils.getInstance().put(AppConfig.PLACE_NAME, "")
                            AppConfig.employeeId = 0
                            AppConfig.currentSysTenantNo = 0
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
            .cancelCompanyApply(
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("解绑公司超时!")
                            return
                        }
                        if (result?.code == 100) {
                            ToastUtils.showLong("解绑公司成功!")
                            SPUtils.getInstance().put(AppConfig.PLACE_NAME, "")
                            AppConfig.employeeId = 0
                            AppConfig.currentSysTenantNo = 0
                            activity?.onBackPressed()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }

}
