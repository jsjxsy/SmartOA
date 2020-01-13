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
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
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
    private var contentList: MutableList<DevDto>? = null
    private var roomId: Long = 0L
    private lateinit var smartSN: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.evnironmental_control_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EnvironmentalControlViewModel::class.java)
        roomId = SPUtils.getInstance().getLong(AppConfig.ROOM_ID, 0L)
        smartSN = SPUtils.getInstance().getString(AppConfig.SMART_HOME_SN, "")
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
        mPagerAdapter = PageAdapter(childFragmentManager)
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = 3
        id_environmental_control_tab.setupWithViewPager(viewPager)
    }


    /**
     * 获取场景列表
     */
    private fun getZGSceneList() {
        sceneListTask = UnisiotApiService.getInstance().areaSceneList(roomId.toString(),
            smartSN, object : CallBack<AreaSceneListResp>() {
                override fun callBack(result: AreaSceneListResp?) {
                    if(!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }

                    if (result == null) {
                        return
                    }
                    when (result.code) {
                        100 -> {
                            val contentList = result.contentList
                            items.clear()
                            if(contentList.isEmpty()){
                                NoDataLayout.visibility = View.VISIBLE
                            }
                            for (content in contentList) {
                                val headTextItem = LightHeadItem(
                                    content.sceneName,
                                    ApiUtils.getImageResource(content.iconSign)
                                )
                                items.add(headTextItem)
                            }
                            val onItemClickListener =
                                object : LightHeadItemViewBinder.OnItemClickListener {
                                    override fun onClick(view: View, position: Int) {
                                        showLoadingView()
                                        executeScene(contentList[position].sceneId)
                                    }
                                }
                            headItemView.onItemClickListener = onItemClickListener
                            adapter.items = items
                            adapter.notifyDataSetChanged()
                        }
                        104 -> {
                            NoDataLayout.visibility = View.VISIBLE
                            tip.text = "网关离线了"
                        }
                        else -> {
                            NoDataLayout.visibility = View.VISIBLE
                            tip.text = "网关离线了"
                        }
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
                    if(!ActivityUtils.isActivityAlive(activity)) {
                        return
                    }

                    if (result == null) {
                        ToastUtils.showLong("执行场景超时")
                        return
                    }
                    if (result.code == 100) {
                        when (result.result) {
                            0 -> showLoadingSuccess()
                            100 -> showLoading()
                            else -> showLoadingFail()
                        }
                    } else {
                        ToastUtils.showLong(result.msg)
                    }
                }
            })
    }

    private fun getDevList() {
        if (GrpcAsyncTask.isFinish(devListTask)) {
            devListTask = UnisiotApiService.getInstance()
                .areaDeviceList(
                    roomId.toString(),
                    smartSN,
                    object : CallBack<AreaDeviceListResp>() {
                        override fun callBack(result: AreaDeviceListResp?) {
                            if(!ActivityUtils.isActivityAlive(activity)) {
                                return
                            }
                            refreshLayout.finishRefresh()
                            if (result == null) {
                                ToastUtils.showLong("获取设备列表超时")
                                showLoadingFail()
                                return
                            }

                            if (result?.code == 100 && result?.result == 0) {
                                contentList = result.contentList
                                updateDev(contentList!!)
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
                    updateDevStatus(notifyBean)
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
        var userId = SPUtils.getInstance().getString(AppConfig.USER_ID)
        var token = SPUtils.getInstance().getString(AppConfig.LOGIN_TOKEN)
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

    private fun updateDevStatus(
        notifyBean: WsNotifyBean
    ) {
        if (contentList == null) {
            return
        }

        var position = 0
        var newDevBean: DevDto? = null
        for (index in contentList!!.indices) {
            val devBean = contentList!![index]
            if ((notifyBean.uuid.compareTo(devBean.uuid, ignoreCase = true) == 0)
                && notifyBean.category.compareTo(devBean.category, ignoreCase = true) == 0
                && notifyBean.model.compareTo(devBean.model, ignoreCase = true) == 0
            ) {
                //去掉后面多余的逗号
                val value = notifyBean.getVal().toString()
                if (0 != value.compareTo(devBean.getVal(), ignoreCase = true)) {
                    position = index
                    newDevBean = DevDto
                        .newBuilder(devBean)
                        .setVal(value)
                        .build()


                }
                //不需要批量更新，应当break
                break
            }
        }
        if (newDevBean != null && contentList != null) {
            contentList!!.removeAt(position)
            contentList!!.add(position, newDevBean)
            updateDev(contentList!!)
        }

    }


    private fun updateDev(contentList: List<DevDto>) {
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
    }

}
