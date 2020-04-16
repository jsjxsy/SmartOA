package com.gx.smart.smartoa.activity.ui.intelligence.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.gx.smart.lib.base.BaseFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.intelligence.viewmodel.IntelligenceParkingViewModel
import kotlinx.android.synthetic.main.car_query_fragment.*


class IntelligenceParkingFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            IntelligenceParkingFragment()
    }

    private lateinit var viewModel: IntelligenceParkingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.intelligence_parking_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(IntelligenceParkingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun initContent() {
        query.setOnClickListener {
            findNavController().navigate(R.id.action_intelligenceParkingFragment_to_carQueryFragment)
        }
    }

}
