package com.gx.smart.smartoa.activity.ui.environmental

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class AirConditionerFragment : Fragment() {

    companion object {
        fun newInstance() = AirConditionerFragment()
        const val AIR_CONDITIONER_TYPE = 2
    }

    private lateinit var viewModel: AirConditionerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.air_conditioner_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AirConditionerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
