package com.gx.smart.smartoa.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.smartoa.activity.ui.login.LoginViewModel

class LoginModelFactory(private val repository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}