package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import com.gx.smart.lib.base.BaseFragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.company.model.SysTenantList
import com.gx.smart.smartoa.activity.ui.features.Divider
import com.gx.smart.smartoa.activity.ui.features.DividerViewBinder
import com.gx.smart.lib.base.BaseFragment
import com.gx.smart.lib.http.api.AppStructureService
import com.gx.smart.lib.http.base.CallBack
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
        initContent()
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

    private fun initContent() {
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val drawable = resources.getDrawable(R.drawable.shape_company_select_are_list_line, null)
        divider.setDrawable(drawable)
        selectAreaRecyclerView.addItemDecoration(divider)

        adapter.register(CompanyAreaCityViewBinder())
        adapter.register(CompanyAreaPlaceViewBinder())
        adapter.register(DividerViewBinder())
        selectAreaRecyclerView.adapter = adapter
        getSysTenantList()

        searchArea.setOnEditorActionListener(OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode === KeyEvent.KEYCODE_ENTER) {
                val name = searchArea.text.toString()
                if (TextUtils.isEmpty(name)) {
                    getSysTenantList()
                } else {
                    getSysTenantListByName(name)
                }
                return@OnEditorActionListener true
            }
            false
        })

    }


    private fun getSysTenantList() {
        AppStructureService.getInstance()
            .getSysTenantList(
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if(!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }

                        items.clear()
                        if (result == null) {
                            ToastUtils.showLong("获取办公地点超时!")
                            return
                        }
                        if (result.code == 100) {
                            val sysTenantsList =
                                JSON.parseArray(result.jsonstr, SysTenantList::class.java)

                            if (sysTenantsList.isNotEmpty()) {
                                for (sysTenants in sysTenantsList) {
                                    val item1 = CompanyAreaCity(sysTenants.title ?: "")
                                    items.add(item1)
                                    if(sysTenants.sysTenants != null && sysTenants.sysTenants.isNotEmpty()){
                                        for (sysTenant in sysTenants.sysTenants) {
                                            val item11 =
                                                CompanyAreaPlace(sysTenant)
                                            items.add(item11)
                                        }
                                    }
                                    items.add(Divider())

                                }
                            }
                            adapter.items = items
                            adapter.notifyDataSetChanged()
                        }
                    }

                })
    }


    private fun getSysTenantListByName(name: String) {
        AppStructureService.getInstance()
            .getSysTenantList(name,
                object : CallBack<CommonResponse>() {
                    override fun callBack(result: CommonResponse?) {
                        if(!ActivityUtils.isActivityAlive(activity)) {
                            return
                        }
                        items.clear()
                        if (result == null) {
                            ToastUtils.showLong("获取办公地点超时!")
                            return
                        }
                        if (result.code == 100) {
                            val sysTenantsList =
                                JSON.parseArray(result.jsonstr, SysTenantList::class.java)

                            if (sysTenantsList.isNotEmpty()) {
                                for (sysTenants in sysTenantsList) {
                                    val item1 = CompanyAreaCity(sysTenants.title ?: "")
                                    items.add(item1)
                                    if(sysTenants.sysTenants != null && sysTenants.sysTenants.isNotEmpty()) {
                                        for (sysTenant in sysTenants.sysTenants) {
                                            val item11 =
                                                CompanyAreaPlace(sysTenant)
                                            items.add(item11)
                                        }
                                    }
                                    items.add(Divider())

                                }
                            }
                            adapter.items = items
                            adapter.notifyDataSetChanged()
                        } else {
                            ToastUtils.showLong(result.msg)
                        }
                    }

                })
    }

}
