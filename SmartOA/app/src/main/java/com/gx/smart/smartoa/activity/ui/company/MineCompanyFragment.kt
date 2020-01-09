package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.AppEmployeeService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.widget.LoadingView
import com.gx.wisestone.work.app.grpc.employee.AppMyCompanyResponse
import com.gx.wisestone.work.app.grpc.employee.EmployeeInfo
import kotlinx.android.synthetic.main.fragment_mine_company.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyFragment : Fragment(), View.OnClickListener {
    var mLoadingView: LoadingView? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_mine_company,
            container,
            false
        )

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        myCompany()
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
        mLoadingView = loadingView

    }


    private fun myCompany() {
        mLoadingView?.visibility = View.VISIBLE
        AppEmployeeService.getInstance()
            .myCompany(
                object : CallBack<AppMyCompanyResponse>() {
                    override fun callBack(result: AppMyCompanyResponse?) {
                        if(!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }

                        mLoadingView?.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("查询我的企业超时!")
                            return
                        }
                        if (result.code == 100) {
                            val employeeList = result.employeeInfoList
                            if (employeeList.isEmpty()) {
                                toMineCompanyFragmentAdd()
                            } else {
                                toMineCompanyFragmentAdded(employeeList[0])
                            }
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }

    private fun toMineCompanyFragmentAdd() {
        if (!ActivityUtils.isActivityAlive(activity)) {
            return
        }
        if (isAdded) {
            val fm: FragmentManager = childFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            transaction.replace(R.id.content, MineCompanyFragmentAdd())
            transaction.commit()
        }
    }

    private fun toMineCompanyFragmentAdded(employeeInfo: EmployeeInfo) {
        if (!ActivityUtils.isActivityAlive(activity)) {
            return
        }

        if (isAdded) {
            val fm: FragmentManager = childFragmentManager
            val transaction: FragmentTransaction = fm.beginTransaction()
            val fragment = MineCompanyFragmentAdded()
            fragment.employeeInfo = employeeInfo
            transaction.replace(R.id.content, fragment)
            transaction.commit()
        }
    }
}
