package com.gx.smart.module.login.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.module.login.mvvm.repository.LoginRepository
import com.gx.smart.module.login.mvvm.viewmodel.LoginViewModel

class LoginModelFactory(private val repository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}