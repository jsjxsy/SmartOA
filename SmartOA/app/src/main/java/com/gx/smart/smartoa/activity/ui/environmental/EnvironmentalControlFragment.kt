package com.gx.smart.smartoa.activity.ui.environmental

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.environmental.utils.ApiUtils
import com.gx.smart.smartoa.activity.ui.environmental.utils.ZGManager
import com.gx.smart.smartoa.activity.ui.environmental.utils.ZGUtil
import com.gx.smart.smartoa.activity.ui.environmental.websocket.WebSocketClientService
import com.gx.smart.smartoa.activity.ui.environmental.websocket.WebSocketNotifyClient
import com.gx.smart.smartoa.activity.ui.environmental.websocket.WsNotifyBean
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.UnisiotApiService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.smart.smartoa.widget.LoadingView
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AreaDeviceListResp
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.AreaSceneListResp
import com.gx.wisestone.service.grpc.lib.smarthome.unisiot.DevDto
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

    private var devListTask: GrpcAsyncTask<String, Void, AreaDeviceListResp>? = null
    private var sceneComTask: GrpcAsyncTask<String, Void, UnisiotResp>? = null
    private lateinit var headItemView: LightHeadItemViewBinder
    private lateinit var sceneListTask: GrpcAsyncTask<String, Void, AreaSceneListResp>
    private lateinit var mPagerAdapter: PageAdapter

    private lateinit var viewModel: EnvironmentalControlViewModel
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    private lateinit var mLoadingView: LoadingView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.evnironmental_control_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EnvironmentalControlViewModel::class.java)
        startWebSocketClient()
        initTitle()
        initHead()
        initContent()
        mLoadingView = loadingView
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        left_nav_image_view.setOnClickListener(this)
        center_title.visibility = View.VISIBLE
        center_title.text = getString(R.string.environmental_control)
    }

    private fun initHead() {
        refreshLayout.setOnRefreshListener {
            getZGSceneList()
            getDevList()
        }
        refreshLayout.isEnableLoadmore = false
        headItemView = LightHeadItemViewBinder()
        adapter.register(headItemView)
        id_environmental_control_head.adapter = adapter
        getZGSceneList()

    }

    private fun initContent() {
        getDevList()
        mPagerAdapter = PageAdapter(childFragmentManager!!)
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        id_environmental_control_tab.setupWithViewPager(viewPager)
    }


    /**
     * 获取场景列表
     */
    private fun getZGSceneList() {
        sceneListTask = UnisiotApiService.getInstance().areaSceneList(AppConfig.ROOM_ID,
            AppConfig.SMART_HOME_SN, object : CallBack<AreaSceneListResp>() {
                override fun callBack(result: AreaSceneListResp?) {
                    if (result == null) {
                        return
                    }
                    if (result?.code == 100) {
                        val contentList = result.contentList
                        items.clear()
                        for (content in contentList) {
                            val headTextItem = LightHeadItem(
                                content.sceneName,
                                ApiUtils.getImageResource(content.iconSign)
                            )
                            items.add(headTextItem)
                        }
                        adapter.items = items
                        val onItemClickListener =
                            object : LightHeadItemViewBinder.OnItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    showLoadingView()
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
                    if (result?.code == 100) {
                        when (result.result) {
                            0 -> showLoadingSuccess()
                            100 -> showLoading()
                            else -> showLoadingFail()
                        }
                    } else {
                        ToastUtils.showLong(result?.msg)
                    }
                }
            })
    }

    private fun getDevList() {
        if (GrpcAsyncTask.isFinish(devListTask)) {
            devListTask = UnisiotApiService.getInstance()
                .areaDeviceList(
                    AppConfig.ROOM_ID,
                    AppConfig.SMART_HOME_SN,
                    object : CallBack<AreaDeviceListResp>() {
                        override fun callBack(result: AreaDeviceListResp?) {
                            refreshLayout.finishRefresh()
                            if (result == null) {
                                ToastUtils.showLong("获取设备列表超时")
                                showLoadingFail()
                                return
                            }

                            if (result?.code == 100 && result?.result == 0) {
                                val contentList = result.contentList
                                //显示对应设备列表
                                val lightList: List<DevDto> = ZGUtil.getDevList(
                                    ZGManager.DEV_TYPE_LIGHT, contentList
                                )
                                val titles =
                                    resources.getStringArray(R.array.environmental_control_items)
                                mPagerAdapter.clearPage()
                                if (lightList.isNotEmpty()) {
                                    mPagerAdapter.addPage(
                                        PageAdapter.PageFragmentContent(
                                            titles[0],
                                            LightFragment.LIGHT_TYPE,
                                            lightList,
                                            this@EnvironmentalControlFragment
                                        )
                                    )
                                }
                                val curtainList: List<DevDto> = ZGUtil.getDevList(
                                    ZGManager.DEV_TYPE_CURTAIN, contentList
                                )

                                if (curtainList.isNotEmpty()) {
                                    mPagerAdapter.addPage(
                                        PageAdapter.PageFragmentContent(
                                            titles[1],
                                            CurtainFragment.CURTAIN_TYPE,
                                            curtainList,
                                            this@EnvironmentalControlFragment
                                        )
                                    )
                                }

                                val airConditionerList: List<DevDto> = ZGUtil.getDevList(
                                    ZGManager.DEV_TYPE_AIR_CONDITIONER, contentList
                                )

                                if (airConditionerList.isNotEmpty()) {
                                    mPagerAdapter.addPage(
                                        PageAdapter.PageFragmentContent(
                                            titles[2],
                                            AirConditionerFragment.AIR_CONDITIONER_TYPE,
                                            airConditionerList,
                                            this@EnvironmentalControlFragment
                                        )
                                    )
                                }

                                val freshAirList: List<DevDto> = ZGUtil.getDevList(
                                    ZGManager.DEV_TYPE_NEW_WIND, contentList
                                )

                                if (freshAirList.isNotEmpty()) {
                                    mPagerAdapter.addPage(
                                        PageAdapter.PageFragmentContent(
                                            titles[3],
                                            FreshAirFragment.FRESH_AIR_TYPE,
                                            freshAirList,
                                            this@EnvironmentalControlFragment
                                        )
                                    )
                                }
                                mPagerAdapter.notifyDataSetChanged()
                                viewPager.offscreenPageLimit = 3
                            } else {
                                ToastUtils.showLong(result?.msg)
                                showLoadingFail()
                            }
                        }

                    })
        }
    }

    fun showLoadingView() {
        mLoadingView.visibility = View.VISIBLE
        mLoadingView.setOnClickListener(null)
    }

    fun showLoading() {
        mLoadingView.setText("执行中")
        mLoadingView.showLoading()
    }

    fun showLoadingSuccess() {
        mLoadingView.setText("执行成功")
        mLoadingView.showSuccess()
        Handler().postDelayed({ mLoadingView.visibility = View.GONE }, 1000)
    }

    fun showLoadingFail() {
        mLoadingView.setText("执行失败")
        mLoadingView.showFail()
        Handler().postDelayed({ mLoadingView.visibility = View.GONE }, 1000)
    }


    var mNotifyHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            if (bundle != null) {
                val notifyBean: WsNotifyBean =
                    bundle.getParcelable<Parcelable>("notifyBean") as WsNotifyBean
                if (null != notifyBean) {
                    Log.e("joylife", "notifyBean:" + notifyBean.getVal())
                    //更新UI设备状态
                    //updateDevStatus(notifyBean)
                }
            }
        }
    }


    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            WebSocketNotifyClient.getInstance().handler = mNotifyHandler
            Log.d("joylife", "onServiceConnected  ")
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    private fun startWebSocketClient() {
        var userId = AppConfig.userId
        var token = AppConfig.loginToken
        val wsClient: WebSocketNotifyClient = WebSocketNotifyClient.getInstance()
        wsClient.handler = mNotifyHandler
        val wsInent = Intent(this.context, WebSocketClientService::class.java)
        wsInent.action = "android.intent.action.RESPOND_VIA_MESSAGE"
        wsInent.putExtra("id", userId)
        wsInent.putExtra("token", token)
        context!!.bindService(
            wsInent,
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun updateDevAdapter(
        //adapter: BaseQuickAdapter<DevBean, BaseViewHolder>,
        notifyBean: WsNotifyBean
    ) {
        var idx = 0
//        for (devBean in adapter.getData()) {
//            if (notifyBean.uuid.compareToIgnoreCase("" + devBean.getUuid()) === 0
//                && notifyBean.category.compareToIgnoreCase(devBean.getCategory()) === 0
//                && notifyBean.model.compareToIgnoreCase(devBean.getModel()) === 0
//            ) {
//                val value = "" + notifyBean.getVal()
//                //去掉后面多余的逗号
//                if (0 != value.compareTo(devBean.getVal(), ignoreCase = true)) {
//                    devBean.setVal(value)
//                    adapter.notifyItemChanged(idx)
//                }
//                //不需要批量更新，应当break
//                break
//            }
//            idx++
//        }
    }


}
