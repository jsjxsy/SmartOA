package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.company.model.Company
import com.gx.smart.smartoa.data.network.api.AppStructureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_mine_company_select_company.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanySelectCompanyFragment : Fragment(), View.OnClickListener {
    lateinit var placeName:String
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeName = it.getString(ARG_PLACE_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_company_select_company, container, false)
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
            it.text = placeName
        }

    }

    lateinit var adapter: CompanyAdapter
    private fun initContent() {

        adapter = CompanyAdapter()
        recyclerView.adapter = adapter
        getBuildingInfo()

        val onItemClick =
            object : CompanyAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    var bundle = Bundle()
                    bundle.putInt(MineCompanyEmployeesFragment.ARG_COMPANY_ID, adapter.mList!![position].id)
                    bundle.putString(MineCompanyEmployeesFragment.ARG_COMPANY_NAME, adapter.mList!![position].name)
                    bundle.putString(MineCompanyEmployeesFragment.ARG_BUILDING_NAME, placeName)

                    Navigation.findNavController(view)
                        .navigate(
                            R.id.action_mineCompanySelectCompanyFragment_to_mineCompanyEmployeesFragment,
                            bundle
                        )
                }

            }

        adapter.onItemClick = onItemClick
    }


    private fun getBuildingInfo() {
        AppStructureService.getInstance()
            .getBuildingInfo(
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if (result == null) {
                            ToastUtils.showLong("添加超时!")
                            return
                        }
                        if (result?.code == 100) {
                            val companyList =
                                JSON.parseArray(result.jsonstr, Company::class.java).toList()
                            adapter.mList = companyList
                            adapter.notifyDataSetChanged()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }

                    }

                })
    }

    companion object {
        const val ARG_PLACE_NAME = "place_name"
    }
}
