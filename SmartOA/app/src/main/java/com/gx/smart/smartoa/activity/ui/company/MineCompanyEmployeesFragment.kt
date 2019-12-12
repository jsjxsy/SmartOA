package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.google.protobuf.ByteString
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppStructureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_mine_company_employees.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyEmployeesFragment : Fragment(), View.OnClickListener {

    private var companyId: Int = 0
    private lateinit var companyName: String
    private var employeeName: String? = null
    private var phoneNumber: String? = null
    private var image: ByteString? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitApply -> {
                employeeName = employeeNameEdit.text.toString()
                phoneNumber = phone.text.toString()
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.showLong("手机号不能为空")
                } else if (phoneNumber!!.length != 11 || !DataCheckUtil.isMobile(phoneNumber)) {
                    ToastUtils.showLong("非法手机号")
                } else if (TextUtils.isEmpty(employeeName)) {
                    ToastUtils.showLong("姓名不能为空")
                } else if (image != null) {
                    ToastUtils.showLong("工牌不能为空")
                }
                applyEmployee(employeeName!!, phoneNumber!!, image!!, companyId.toLong())
            }
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            companyId = it.getInt(ARG_COMPANY_ID)
            companyName = it.getString(ARG_COMPANY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_company_employees, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            it.text = getString(R.string.mine_company)
        }

    }

    private fun initContent() {
        companyNameText.text = companyName
        submitApply.setOnClickListener(this)
    }


    private fun applyEmployee(
        name: String,
        mobile: String,
        image: ByteString,
        companyId: Long
    ) {
        AppStructureService.getInstance()
            .applyEmployee(name, mobile, image, companyId,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("添加超时!")
                            return
                        }
                        if (result?.code == 100) {
                            ToastUtils.showLong("申请成功!")
                            activity?.finish()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }

    companion object {
        const val ARG_COMPANY_ID = "company_id"
        const val ARG_COMPANY_NAME = "company_name"
    }

}
