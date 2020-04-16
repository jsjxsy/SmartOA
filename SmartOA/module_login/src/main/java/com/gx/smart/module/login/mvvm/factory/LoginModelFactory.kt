package com.gx.smart.module.login.mvvm.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.module.login.mvvm.repository.LoginRepository
import com.gx.smart.module.login.mvvm.viewmodel.ForgetPasswordViewModel
import com.gx.smart.module.login.mvvm.viewmodel.LoginViewModel
import com.gx.smart.module.login.mvvm.viewmodel.RegisterViewModel

class LoginModelFactory(private val application: Application,private val repository: LoginRepository) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(application,repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java)  -> RegisterViewModel(application, repository) as T
            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)  -> ForgetPasswordViewModel(application, repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

    }
}