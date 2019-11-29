package com.gx.smart.smartoa.activity.ui.repair

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class RepairTypeFragment : Fragment() {

    companion object {
        fun newInstance() = RepairTypeFragment()
    }

    private lateinit var viewModel: RepairTypeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repair_type_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RepairTypeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
