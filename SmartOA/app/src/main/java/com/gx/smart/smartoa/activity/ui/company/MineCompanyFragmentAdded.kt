package com.gx.smart.smartoa.activity.ui.company


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import com.gx.wisestone.work.app.grpc.employee.EmployeeInfo
import kotlinx.android.synthetic.main.fragment_mine_company_added.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyFragmentAdded : Fragment(), View.OnClickListener {
    lateinit var employeeInfo: EmployeeInfo
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.unbindCompany -> cancelCompanyBind()
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
        initTitle()
        initContent()
        unbindCompany.setOnClickListener(this)
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.mine_company)
        }

    }


    private fun initContent() {
        Glide.with(this).load(employeeInfo.imageUrl).into(logoCompany)
        companyName.text = employeeInfo.companyName
        companyDepartment.text = employeeInfo.departmentName
        verify.text = when (employeeInfo.status) {
            1 -> {
                verify.setTextColor(Color.YELLOW)
                "审核中"
            }
            2 -> {
                verify.setTextColor(Color.GREEN)
                "已认证"
            }
            else -> {
                verify.setTextColor(Color.RED)
                "拒绝"
            }
        }
        Glide.with(this).load(employeeInfo.imageUrl).into(logoCompany)
    }

    private fun cancelCompanyBind() {
        AppEmployeeService.getInstance()
            .cancelCompanyBind(
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("添加超时!")
                            return
                        }
                        if (result?.code == 100) {

                        }
                    }

                })
    }

}
