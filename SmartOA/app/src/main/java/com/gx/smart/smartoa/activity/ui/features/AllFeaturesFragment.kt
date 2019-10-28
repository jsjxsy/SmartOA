package com.gx.smart.smartoa.activity.ui.features

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class AllFeaturesFragment : Fragment() {

    companion object {
        fun newInstance() = AllFeaturesFragment()
    }

    private lateinit var viewModel: AllFeaturesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_features_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllFeaturesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
