package com.gx.smart.smartoa.activity.ui.air

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class AirQualityFragment : Fragment() {

    companion object {
        fun newInstance() = AirQualityFragment()
    }

    private lateinit var viewModel: AirQualityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.air_quality_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AirQualityViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
