package com.gx.smart.lib.base

import androidx.fragment.app.Fragment
import com.drakeet.multitype.MultiTypeAdapter

/**
 *@author xiaosy
 *@create 2019-10-31
 *@Describe
 **/
open class BaseFragment: Fragment() {
    open val adapter = MultiTypeAdapter()
    open val items = ArrayList<Any>()
}