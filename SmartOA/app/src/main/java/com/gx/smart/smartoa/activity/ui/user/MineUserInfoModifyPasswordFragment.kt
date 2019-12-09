package com.gx.smart.smartoa.activity.ui.user


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
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
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.smart.smartoa.data.network.api.base.GrpcAsyncTask
import com.gx.smart.smartoa.data.network.api.lib.utils.AuthUtils
import com.gx.smart.smartoa.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.UserModifyResp
import com.gx.wisestone.uaa.grpc.lib.auth.VerifyCodeResp
import kotlinx.android.synthetic.main.fragment_mine_user_info_modify_password.*
import kotlinx.android.synthetic.main.layout_common_title.*

class MineUserInfoModifyPasswordFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.save -> save()
        }
    }


    private var userModifyPassWordTask: GrpcAsyncTask<String, Void, UserModifyResp>? = null
    private var userModifyPassWordTaskCallBack: CallBack<UserModifyResp>? = null
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
        return inflater.inflate(R.layout.fragment_mine_user_info_modify_password, container, false)
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
            it.text = getString(R.string.modify_password)
        }
    }

    private fun initContent() {
        initVerifyCode()
        mLoadingView = loadingView
        phoneNumber.text = phone?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        initVerifyCode()
        save.setOnClickListener(this)
    }


    private fun save() {
        mLoadingView.visibility = View.VISIBLE
        val password: String = newPassword.text.toString().trim()
        val confirmPassword: String = confirmNewPassword.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showLong("密码不能为空")
        } else if (doExcute(password) < 2) {
            ToastUtils.showLong("密码过于简单")
        } else if (password.length < 8) {
            ToastUtils.showLong("密码长度不得小于8位")
        } else if (password.length > 16) {
            ToastUtils.showLong("密码长度不得大于16位")
        } else if (TextUtils.isEmpty(confirmPassword)) {
            ToastUtils.showLong("确认密码不能为空")
        } else if (password != confirmPassword) {
            ToastUtils.showLong("确认密码与密码不一致")
        } else {
            appModifyPassword()
            val token = AppConfig.loginToken
            val holder = AuthUtils.parseJwtHolder(token)
            val userId = holder.subject
            if (GrpcAsyncTask.isFinish(userModifyPassWordTask)) {
                userModifyPassWordTask = AuthApiService.getInstance()
                    .userModifyPassWord(password, userId, token, userModifyPassWordTaskCallBack)
            }
        }
    }

    /*******************************************修改密码回调 */
    private fun appModifyPassword() {
        userModifyPassWordTaskCallBack = object : CallBack<UserModifyResp>() {
            override fun callBack(result: UserModifyResp?) {
                mLoadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("修改密码超时")
                    return
                }
                val msg = result.dataMap["errMsg"]
                if (result?.code == 100) {
                    ToastUtils.showLong("修改密码成功")
                    activity?.finish()
                    ActivityUtils.startActivity(Intent(activity!!, LoginActivity::class.java))
                } else {
                    ToastUtils.showLong(msg)
                }
            }
        }
    }

    private fun initVerifyCode() {
        verifyCodeText = getVerifyCodeText.apply {
            this.setOnClickListener {
                getVerifyCode()
            }
        }
        mTime = TimeCount(60000, 1000, verifyCodeText)
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
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络连接不可用")
            return
        }
        getVerifyCodeCallback()
        val targetType = 1
        val purpose = 2
        if (GrpcAsyncTask.isFinish(verifyTask)) {
            verifyTask = AuthApiService.getInstance()
                .verifyCode(phone, targetType, purpose, verifyCallBack)
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
                    mLoadingView.visibility = View.GONE
                    val userId = result.dataMap["userId"]
                    if (!TextUtils.isEmpty(userId)) {
                        activity?.finish()
                        ActivityUtils.startActivity(Intent(activity, LoginActivity::class.java))
                    }
                }
            }
        }
    }

    companion object {
        const val ARG_PHONE_NUMBER = "phoneNumber"
    }


    /*******************************************密码校验 */
    private fun doExcute(password: String): Int {
        var kindOfCharacter = 0
        val digital = "[0-9]"
        val capital = "[A-Z]"
        val lowercase = "[a-z]"
        val spec = "[-~!@#$%^&*()_+{}|:<>?;',.]"
        /** 有大写字母或小写字母，即有字母  */
        for (i in 0 until password.length) {
            if (password.substring(i, i + 1).matches(capital.toRegex())
                || password.substring(i, i + 1).matches(lowercase.toRegex())
            ) {
                println(password.substring(i, i + 1))
                kindOfCharacter++
                break
            }
        }
        /** 有数字  */
        for (i in 0 until password.length) {
            if (password.substring(i, i + 1).matches(digital.toRegex())) {
                kindOfCharacter++
                break
            }
        }
        /** 有符号  */
        for (i in 0 until password.length) {
            if (password.substring(i, i + 1).matches(spec.toRegex())) {
                kindOfCharacter++
                break
            }
        }
        if (password.length < 6) {
            kindOfCharacter = 1
        }
        return kindOfCharacter
    }

}
