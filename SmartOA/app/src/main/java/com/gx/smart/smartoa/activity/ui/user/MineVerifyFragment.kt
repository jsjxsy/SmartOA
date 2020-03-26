package com.gx.smart.smartoa.activity.ui.user


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.UserCenterService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.smart.smartoa.utils.DataCheckUtil.CheckIdCard
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import com.gx.wisestone.work.app.grpc.common.CommonResponse
import kotlinx.android.synthetic.main.fragment_mine_verify.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineVerifyFragment : BaseFragment(), View.OnClickListener {
    private var verifiedCallBack: CallBack<CommonResponse>? = null
    private var verifiedTask: GrpcAsyncTask<String, Void, CommonResponse>? = null
    private var verify: Int = 2
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            verify = it.getInt(ARG_VERIFY)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_verify, container, false)
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
            it.text = getString(R.string.identification_verify)
        }
    }

    private fun initContent() {
        when (verify) {
            1 -> {
                realNameEdit.isEnabled = false
                identificationNumberEdit.isEnabled = false
                save.visibility = View.GONE
                verifyState.visibility = View.VISIBLE
                getUserInfo()
            }
            2 -> {
                realNameEdit.isEnabled = true
                identificationNumberEdit.isEnabled = true
                save.visibility = View.VISIBLE
                verifyState.visibility = View.GONE
                save.setOnClickListener {
                    identityCard()
                }
            }
        }
    }


    private fun identityCard() {
        var name: String = realNameEdit.text.toString().trim()
        var idCard: String = identificationNumberEdit.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showLong("真实姓名不能为空")
        } else if (TextUtils.isEmpty(idCard)) {
            ToastUtils.showLong("身份证号不能为空")
        } else if (idCard.length != 18 || !CheckIdCard(idCard)) {
            ToastUtils.showLong("无效的身份证")
        } else {
            loadingView.visibility = View.VISIBLE
            changeUserIdCard(name)
            if (GrpcAsyncTask.isFinish(verifiedTask)) {
                verifiedTask =
                    UserCenterService.getInstance().verified(name, idCard, verifiedCallBack)
            }
        }
    }


    private fun getUserInfo() {
        UserCenterService.getInstance().getAppUserInfo(object : CallBack<AppInfoResponse>() {
            override fun callBack(result: AppInfoResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                if (result?.code == 100) {
                    val userInfo = result.appUserInfoDto
                    if (userInfo.name.isNotBlank()) {
                        realNameEdit.setText(userInfo.name)
                        SPUtils.getInstance().put(AppConfig.SH_USER_REAL_NAME, userInfo.name)
                        identificationNumberEdit.setText(userInfo.identityCard)
                    }

                }
            }

        })
    }

    /*******************************************修改身份证号回调 */
    private fun changeUserIdCard(name: String) {
        verifiedCallBack = object : CallBack<CommonResponse>() {
            override fun callBack(result: CommonResponse?) {
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                loadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("修改身份证号超时")
                    return
                }
                if (result.code == 100) {
                    ToastUtils.showLong("修改身份证号成功")
                    SPUtils.getInstance().put(AppConfig.SH_USER_REAL_NAME, name)
                    activity?.onBackPressed()
                } else {
                    ToastUtils.showLong(result.msg)
                }
            }
        }
    }

    companion object {
        const val ARG_VERIFY = "verify"
    }


}
