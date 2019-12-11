package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.google.protobuf.ByteString
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppStructureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_mine_company_employees.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyEmployeesFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addCompany -> Navigation.findNavController(v).navigate(R.id.action_mineCompanyFragment_to_mineCompanySelectAreaFragment)
            R.id.left_nav_image_view -> activity?.onBackPressed()
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
                        }
                    }

                })
    }

}
