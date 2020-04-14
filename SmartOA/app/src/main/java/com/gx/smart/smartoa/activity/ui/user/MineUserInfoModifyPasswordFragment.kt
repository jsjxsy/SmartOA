package com.gx.smart.smartoa.activity.ui.user


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.gx.smart.lib.base.BaseFragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.login.LoginActivity
import com.gx.smart.common.AppConfig
import com.gx.smart.lib.http.api.AuthApiService
import com.gx.smart.lib.http.base.CallBack
import com.gx.smart.lib.http.base.GrpcAsyncTask
import com.gx.smart.lib.http.lib.utils.AuthUtils
import com.gx.smart.lib.widget.LoadingView
import com.gx.wisestone.uaa.grpc.lib.auth.UserModifyResp
import kotlinx.android.synthetic.main.fragment_mine_user_info_modify_password.*
import kotlinx.android.synthetic.main.layout_common_title.*

class MineUserInfoModifyPasswordFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.save -> save()
        }
    }


    private var userModifyPassWordTask: GrpcAsyncTask<String, Void, UserModifyResp>? = null
    private var userModifyPassWordTaskCallBack: CallBack<UserModifyResp>? = null
    private var phone: String? = null
    private lateinit var mLoadingView: LoadingView


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

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initContent()
    }

    override fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.modify_password)
        }
    }

    override fun initContent() {
        mLoadingView = loadingView
        phoneNumber.text = phone?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        save.setOnClickListener(this)
        newPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        newPassword.transformationMethod =
            PasswordTransformationMethod.getInstance()

        confirmNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        confirmNewPassword.transformationMethod =
            PasswordTransformationMethod.getInstance()


        newPasswordState.setOnClickListener {
            passwordState(newPassword, newPasswordState)
        }
        confirmNewPasswordState.setOnClickListener {
            passwordState(confirmNewPassword, confirmNewPasswordState)
        }
    }


    private fun save() {
        mLoadingView.visibility = View.VISIBLE
        val password: String = newPassword.text.toString().trim()
        val confirmPassword: String = confirmNewPassword.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showLong("密码不能为空")
            mLoadingView.visibility = View.GONE
        } else if (doExcute(password) < 2) {
            ToastUtils.showLong("密码过于简单")
            mLoadingView.visibility = View.GONE
        } else if (password.length < 8) {
            ToastUtils.showLong("密码长度不得小于8位")
            mLoadingView.visibility = View.GONE
        } else if (password.length > 16) {
            ToastUtils.showLong("密码长度不得大于16位")
        } else if (TextUtils.isEmpty(confirmPassword)) {
            ToastUtils.showLong("确认密码不能为空")
            mLoadingView.visibility = View.GONE
        } else if (password != confirmPassword) {
            ToastUtils.showLong("确认密码与密码不一致")
            mLoadingView.visibility = View.GONE
        } else {
            appModifyPassword()
            val token = SPUtils.getInstance().getString(AppConfig.LOGIN_TOKEN)
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
                if (!ActivityUtils.isActivityAlive(activity)) {
                    return
                }
                mLoadingView.visibility = View.GONE
                if (result == null) {
                    ToastUtils.showLong("修改密码超时")
                    return
                }

                if (result.code == 100) {
                    ToastUtils.showLong("修改密码成功")
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    ActivityUtils.startActivity(Intent(activity!!, LoginActivity::class.java))
                } else {
                    val msg = result.dataMap["errMsg"]
                    ToastUtils.showLong(msg)
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
        for (i in password.indices) {
            if (password.substring(i, i + 1).matches(capital.toRegex())
                || password.substring(i, i + 1).matches(lowercase.toRegex())
            ) {
                println(password.substring(i, i + 1))
                kindOfCharacter++
                break
            }
        }
        /** 有数字  */
        for (i in password.indices) {
            if (password.substring(i, i + 1).matches(digital.toRegex())) {
                kindOfCharacter++
                break
            }
        }
        /** 有符号  */
        for (i in password.indices) {
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


    private fun passwordState(editText: EditText,imageView: ImageView) {

        when (editText.inputType) {
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                imageView.setImageResource(R.drawable.ic_login_password_state_visible)
                editText.inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                //不可见变为可见
                //设置密码为明文，并更改眼睛图标
                editText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }

            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> {
                imageView.setImageResource(R.drawable.ic_login_password_state)
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                //可见变为不可见
                editText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
        editText.setSelection(editText.text.length)

    }

}
