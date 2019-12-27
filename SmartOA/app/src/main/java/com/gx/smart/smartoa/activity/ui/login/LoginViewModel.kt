package com.gx.smart.smartoa.activity.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.SPUtils
import com.gx.smart.smartoa.data.network.AppConfig

class LoginViewModel : ViewModel() {
    var phone = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")


    fun setPhone() {
        phone.value = SPUtils.getInstance().getString(AppConfig.SH_USER_ACCOUNT, "")
    }

    override fun toString(): String {
        return "phone $phone password $password"
    }
}
