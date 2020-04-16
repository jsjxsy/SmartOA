package com.gx.smart.smartoa.activity.ui.intelligence.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.lib.base.BaseFragment
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.intelligence.viewmodel.CarPaymentRecordViewModel

class CarPaymentRecordFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            CarPaymentRecordFragment()
    }

    private lateinit var viewModel: CarPaymentRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_payment_record_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CarPaymentRecordViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
