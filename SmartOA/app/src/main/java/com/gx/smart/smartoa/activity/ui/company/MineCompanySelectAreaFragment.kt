package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.features.Divider
import com.gx.smart.smartoa.activity.ui.features.DividerViewBinder
import com.gx.smart.smartoa.base.BaseFragment
import com.gx.smart.smartoa.data.network.api.AppStructureService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_mine_company_select_area.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanySelectAreaFragment : BaseFragment(), OnClickListener {
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
        return inflater.inflate(R.layout.fragment_mine_company_select_area, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initData()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.select_office_area)
        }

    }

    private fun initData() {
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_company_select_are_list_line, null)
        divider.setDrawable(drawable)
        selectAreaRecyclerView.addItemDecoration(divider)

        adapter.register(CompanyAreaCityViewBinder())
        adapter.register(CompanyAreaPlaceViewBinder())
        adapter.register(DividerViewBinder())
        selectAreaRecyclerView.adapter = adapter

        val item1 =
            CompanyAreaCity("杭州市")
        val item11 =
            CompanyAreaPlace("悦盛国际中心")
        val item12 =
            CompanyAreaPlace("广孚中心")

        items.add(item1)
        items.add(item11)
        items.add(item12)

        items.add(Divider())

        val item2 =
            CompanyAreaCity("上海市")
        val item21 =
            CompanyAreaPlace("悦盛国际中心")
        val item22 =
            CompanyAreaPlace("广孚中心")
        items.add(item2)
        items.add(item21)
        items.add(item22)

        adapter.items = items
        adapter.notifyDataSetChanged()
    }


    private fun getSysTenantList() {
        AppStructureService.getInstance()
            .getSysTenantList(
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
