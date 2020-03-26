package com.gx.smart.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

/**
 *@author xiaosy
 *@create 2020/3/26
 *@Describe 采取MVVM模式封装Fragment
 **/
 abstract class BaseMVVMFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseFragment() {
    open val viewModel by lazy { createViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val dataBindingUtil = DataBindingUtil.inflate<V>(
            inflater,
            onBindLayout(),
            container,
            false
        )
        onBindViewModelByDataBinding(dataBindingUtil)
        dataBindingUtil.lifecycleOwner = this
        return dataBindingUtil.root
    }

    open fun onBindViewModelByDataBinding(dataBindingUtil: V) {
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this, onBindViewModelFactory()).get(onBindViewModel())
    }

    abstract fun onBindViewModel(): Class<VM>
    abstract fun onBindViewModelFactory(): ViewModelProvider.Factory
}