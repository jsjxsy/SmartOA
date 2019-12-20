package com.gx.smart.smartoa.activity.ui.user

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.smartoa.data.network.AppConfig
import com.gx.smart.smartoa.data.network.api.AuthApiService
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.smart.smartoa.data.network.api.lib.model.JwtHolder
import com.gx.smart.smartoa.data.network.api.lib.utils.AuthUtils
import com.gx.smart.smartoa.utils.DataCheckUtil
import com.gx.smart.smartoa.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.UserModifyResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import kotlinx.android.synthetic.main.fragment_mine_user_info_modify_phone.*
import kotlinx.android.synthetic.main.fragment_mine_user_info_modify_phone.delete
import kotlinx.android.synthetic.main.fragment_mine_user_info_modify_phone.loadingView
import kotlinx.android.synthetic.main.layout_common_title.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MineUserInfoModifyPhoneFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.save -> save()
        }
    }

    private var updateAppUserMobileTask: GrpcAsyncTask<String, Void, AppInfoResponse>? = null
    private var updateAppUserMobileCallBack: CallBack<AppInfoResponse>? = null

    private var userModifyMobileCallBack: CallBack<UserModifyResp>? = null
    private var userModifyMobileTask: GrpcAsyncTask<String, Void, UserModifyResp>? = null

    private var phone: String? = null
    private var mTime: TimeCount? = null
    private lateinit var verifyCodeText: TextView
    private lateinit var mLoadingView: LoadingView

    private var verifyTask: GrpcAsyncTask<String, Void, VerifyCodeResp>? = null
    private var verifyCallBack: CallBack<VerifyCodeResp?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phone = it.getString(ARG_PHONE_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_user_info_modify_phone, container, false)
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
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.modify_phone)
        }
    }

    private fun initContent() {
        mLoadingView = loadingView
        phoneNumber.text = phone?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        initVerifyCode()
        save.setOnClickListener(this)
        delete.setOnClickListener {
            newPhone.editableText.clear()
        }
        newPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                if (length > 0) {
                    delete.visibility = View.VISIBLE
                } else {
                    delete.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun initVerifyCode() {
        verifyCodeText = getVerifyCodeText.apply {
            this.setOnClickListener {
                getVerifyCode()
            }
        }
        mTime = TimeCount(60000, 1000, verifyCodeText)
    }

    private fun save() {
        mLoadingView.visibility = View.VISIBLE
        val token1: String = AppConfig.loginToken
        val holder1: JwtHolder = AuthUtils.parseJwtHolder(token1)
        val userId1: String = holder1.subject
        val verifyCode: String = verifyCode.text.toString().trim()
        val newPhone: String = newPhone.text.toString().trim()
        if (TextUtils.isEmpty(newPhone)) {
            ToastUtils.showLong("手机号码不能为空")
            mLoadingView.visibility = View.GONE
        } else if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showLong("验证码不能为空")
            mLoadingView.visibility = View.GONE
        } else if (verifyCode.length != 6) {
            ToastUtils.showLong("验证码长度不正确")
            mLoadingView.visibility = View.GONE
        } else if (newPhone.length != 11 || !DataCheckUtil.isMobile(newPhone)) {
            ToastUtils.showLong("非法手机号")
            mLoadingView.visibility = View.GONE
        } else if (newPhone == phone) {
            ToastUtils.showLong("与登陆手机号相同")
            mLoadingView.visibility = View.GONE
        } else {
            changePhone(verifyCode)
            if (GrpcAsyncTask.isFinish(userModifyMobileTask)) {
                userModifyMobileTask = AuthApiService.getInstance().userModifyMobile(
                    newPhone,
                    verifyCode,
                    userId1,
                    token1,
                    userModifyMobileCallBack
                )
            }
        }
    }


    /*******************************************修改手机号回调 */
    private fun changePhone(changeNo: String?) {
        userModifyMobileCallBack = object : CallBack<UserModifyResp>() {
            override fun callBack(result: UserModifyResp?) {
                if (result == null) {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong("修改手机号超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result?.code == 100) {
                    mTime!!.start()
                    changeUserMobile()
                    if (GrpcAsyncTask.isFinish(updateAppUserMobileTask)) {
                        updateAppUserMobileTask = UserCenterService.getInstance()
                            .updateAppUserMobile(changeNo, updateAppUserMobileCallBack)
                    }
                } else {
                    mLoadingView.visibility = View.GONE
                    ToastUtils.showLong(msg)
                }
            }
        }
    }

    fun changeUserMobile() {
        updateAppUserMobileCallBack = object : CallBack<AppInfoResponse>() {
            override fun callBack(result: AppInfoResponse?) {
                mLoadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("修改手机号超时")
                    return
                }
                if (result?.code === 100) {
                    ToastUtils.showLong("修改手机号成功")
                    activity?.finish()
                    ActivityUtils.startActivity(activity!!, LoginActivity::class.java)
                } else {
                    ToastUtils.showLong(result?.msg)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mTime?.cancel()
    }

    //获取验证码定时器
    class TimeCount(
        millisInFuture: Long,
        countDownInterval: Long,
        private val verifyCodeText: TextView
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            verifyCodeText.text = "获取验证码"
            verifyCodeText.isClickable = true
        }

        override fun onTick(millisUntilFinished: Long) {
            verifyCodeText.isClickable = false
            verifyCodeText.text = String.format(
                "%s",
                millisUntilFinished.div(1000).toString() + "s"
            )
        }
    }


    private fun getVerifyCode() {
        val phoneNumber = newPhone.text.toString()
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showLong("手机号不能为空")
        } else if (phoneNumber!!.length != 11 || !DataCheckUtil.isMobile(phoneNumber)) {
            ToastUtils.showLong("非法手机号")
        } else {
            getVerifyCodeCallback()
            val targetType = 1
            val purpose = 3
            if (GrpcAsyncTask.isFinish(verifyTask)) {
                verifyTask = AuthApiService.getInstance()
                    .verifyCode(phoneNumber, targetType, purpose, verifyCallBack)
            }
        }
    }

    /*******************************************获取验证码回调 */
    private fun getVerifyCodeCallback() {
        verifyCallBack = object : CallBack<VerifyCodeResp?>() {
            override fun callBack(result: VerifyCodeResp?) {
                if (result == null) {
                    ToastUtils.showLong("验证码请求超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result.code == 100) {
                    mTime?.start()
                    ToastUtils.showLong("获取验证码成功")
                } else {
                    ToastUtils.showLong(msg)
                }
            }
        }
    }


    companion object {
        const val ARG_PHONE_NUMBER = "phoneNumber"
    }
}
