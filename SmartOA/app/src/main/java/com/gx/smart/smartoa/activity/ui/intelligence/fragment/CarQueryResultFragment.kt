package com.gx.smart.smartoa.activity.ui.intelligence.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.intelligence.viewmodel.CarQueryResultViewModel


class CarQueryResultFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            CarQueryResultFragment()
    }

    private lateinit var viewModel: CarQueryResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_query_result_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CarQueryResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
