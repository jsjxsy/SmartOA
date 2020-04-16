package com.gx.smart.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 *@author xiaosy
 *@create 2019-10-31
 *@Describe 基础类
 **/
abstract class BaseFragment : Fragment() {
    open lateinit var mRootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mRootView = inflater.inflate(onBindLayout(), container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitle()
        initContent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initObserver()
    }

    abstract fun onBindLayout(): Int

    open fun initTitle() {

    }

    open fun initContent() {

    }

    open fun initData() {

    }

    open fun initObserver() {

    }


}