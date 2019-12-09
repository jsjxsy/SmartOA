package com.gx.smart.smartoa.activity.ui.environmental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.environmental.bean.ZGDevListBean
import com.gx.smart.smartoa.activity.ui.environmental.utils.ApiUtils
import com.gx.smart.smartoa.activity.ui.environmental.utils.ZGManager
import com.gx.smart.smartoa.activity.ui.environmental.utils.ZGUtil
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.UnisiotApiService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevListResp
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.SceneListResp
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.UnisiotResp
import kotlinx.android.synthetic.main.evnironmental_control_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class EnvironmentalControlFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance() = EnvironmentalControlFragment()
    }

    private var devListTask: GrpcAsyncTask<String, Void, DevListResp>? = null
    private var sceneComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    private lateinit var headItemView: LightHeadItemViewBinder
    private var sceneListTask: GrpcAsyncTask<String, Void, SceneListResp>? = null
    private lateinit var mPagerAdapter: PageAdapter

    private lateinit var viewModel: EnvironmentalControlViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.evnironmental_control_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EnvironmentalControlViewModel::class.java)
        initTitle()
        initHead()
        initContent()
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        left_nav_image_view.setOnClickListener(this)
        center_title.visibility = View.VISIBLE
        center_title.text = getString(R.string.environmental_control)
    }

    private fun initHead() {
        refreshLayout.setOnRefreshListener { refreshLayout.finishRefresh(2000) }
        refreshLayout.isEnableLoadmore = false
        headItemView = LightHeadItemViewBinder()
        adapter.register(headItemView)
        id_environmental_control_head.adapter = adapter
        getZGSceneList()

    }

    private fun initContent() {
        getDevList()
        val titles = resources.getStringArray(R.array.environmental_control_items)
        mPagerAdapter = PageAdapter(fragmentManager!!)
        for (i in 0 until titles.size) {
            mPagerAdapter.addPage(PageAdapter.PageFragmentContent(titles[i], i))
        }
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        mPagerAdapter.notifyDataSetChanged()
        id_environmental_control_tab.setupWithViewPager(viewPager)
    }


    /**
     * 获取场景列表
     */
    private fun getZGSceneList() {
        sceneListTask = UnisiotApiService.getInstance()
            .sceneList(AppConfig.SMART_HOME_SN, object : CallBack<SceneListResp>() {
                override fun callBack(result: SceneListResp?) {
                    if (result == null) {
                        return
                    }
                    if (result?.code == 100) {
                        val contentList = result.contentList
                        for (content in contentList) {
                            val headTextItem = LightHeadItem(
                                content.sceneName,
                                ApiUtils.getImageResouce(content.iconSign)
                            )
                            items.add(headTextItem)
                        }
                        adapter.items = items
                        val onItemClickListener =
                            object : LightHeadItemViewBinder.OnItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    executeScene(contentList[position].sceneId)
                                }
                            }
                        headItemView.onItemClickListener = onItemClickListener
                        adapter.notifyDataSetChanged()
                    } else if (result.code == 104) {
                        NoDataLayout.visibility = View.VISIBLE
                        tip.text = "网关离线了"
                    }
                }
            })
    }

    /**
     * 执行场景
     *
     * @param sceneId 场景ID
     */
    private fun executeScene(sceneId: String) {
        sceneComTask = UnisiotApiService.getInstance().sceneCom(
            AppConfig.SMART_HOME_SN,
            sceneId,
            object : CallBack<UnisiotResp>() {
                override fun callBack(result: UnisiotResp?) {
                    if (result == null) {
                        ToastUtils.showLong("执行场景超时")
                        return
                    }
                    if (result?.code == 100 && result?.result == 0) {

                    } else {
                        ToastUtils.showLong(result?.msg)
                    }
                }
            })
    }

    private fun getDevList() {
        if (GrpcAsyncTask.isFinish(devListTask)) {
            devListTask = UnisiotApiService.getInstance()
                .devList(AppConfig.SMART_HOME_SN, object : CallBack<DevListResp>() {
                    override fun callBack(result: DevListResp?) {
                        if (result == null) {
                            ToastUtils.showLong("获取设备列表超时")
                            return
                        }

                        if (result?.code == 100 && result?.result == 0) {
                            val contentList = result.contentList
                                //显示对应设备列表
//                                val lightList: List<ZGDevListBean.DataResponseBean.DevBean> = ZGUtil.getDevList(
//                                    ZGManager.DEV_TYPE_LIGHT, contentList
//                                )
//                                if (lightList.isNotEmpty()) {
//
//                                }
                        } else {
                            ToastUtils.showLong(result?.msg)
                        }
                    }

                })
        }
    }


}
