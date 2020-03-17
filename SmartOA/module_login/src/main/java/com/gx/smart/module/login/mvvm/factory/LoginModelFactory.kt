package com.gx.smart.module.login.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.module.login.mvvm.repository.LoginRepository
import com.gx.smart.module.login.mvvm.viewmodel.ForgetPasswordViewModel
import com.gx.smart.module.login.mvvm.viewmodel.LoginViewModel
import com.gx.smart.module.login.mvvm.viewmodel.RegisterViewModel

class LoginModelFactory(private val repository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java)  -> RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)  -> ForgetPasswordViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

    }
}