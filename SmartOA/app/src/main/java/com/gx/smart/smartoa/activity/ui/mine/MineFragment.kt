package com.gx.smart.smartoa.activity.ui.mine

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.MainActivity
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MineFragment()
    }

    private lateinit var viewModel: MineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.statusBarColor = Color.TRANSPARENT
        (activity as MainActivity).stateSetting()
        getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MineViewModel::class.java)

        val mineList = mine_setting_list_view as ListView
        val adapter = MineAdapter(activity as Context)
        adapter.lists = arrayListOf(
            Item(
                R.drawable.ic_mine_company, getString(R.string.mine_company)
            ),
            Item(
                R.drawable.ic_mine_meeting, getString(R.string.mine_meeting)
            ),
            Item(
                R.drawable.ic_mine_action, getString(R.string.mine_action)
            ),
            Item(
                R.drawable.ic_mine_visitor, getString(R.string.mine_visitor)
            )
        )
        mineList.adapter = adapter
        mineList.setOnItemClickListener { _, view, position, _ ->
            when (position) {
                0 -> Navigation.findNavController(view).navigate(R.id.action_mineFragment_to_mineCompanyActivity)
                1 -> Navigation.findNavController(view).navigate(R.id.action_mineFragment_to_meetingScheduleActivity)
                2 -> Navigation.findNavController(view).navigate(R.id.action_mineFragment_to_mineActionActivity)
                3 -> Navigation.findNavController(view).navigate(R.id.action_mineFragment_to_visitorActivity)
            }
        }
        setting.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mineFragment_to_settingActivity)
        }
        mine_user_info_text_view.setOnClickListener(this)
    }

    class Item {
        var itemIcon: Int
        var itemName: String

        constructor(itemIcon: Int, itemName: String) {
            this.itemIcon = itemIcon
            this.itemName = itemName
        }
    }


    private fun getUserInfo() {
        UserCenterService.getInstance().getAppUserInfo(object : CallBack<AppInfoResponse>() {
            override fun callBack(result: AppInfoResponse?) {
                if (result?.code == 100) {
                    val userInfo = result.appUserInfoDto
                    if (ActivityUtils.isActivityAlive(activity)) {
                        val headImageUrl =
                            userInfo.imageUrl + "?v=" + SystemClock.currentThreadTimeMillis()
                        Glide.with(activity!!)
                            .load(headImageUrl)
                            .error(R.drawable.ic_head)
                            .placeholder(R.drawable.ic_head)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .skipMemoryCache(true) // 不使用内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(mine_head_image_view)
                        mine_user_name_text_view.text = userInfo.nickName
                    }

                }
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mine_user_info_text_view -> {
                Navigation.findNavController(v).navigate(R.id.action_mineFragment_to_userActivity)
            }
        }
    }


}
