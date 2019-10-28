package com.gx.smart.smartoa.activity.ui.open

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

class OpenDoorFragment : Fragment() {

    companion object {
        fun newInstance() = OpenDoorFragment()
    }

    private lateinit var viewModel: OpenDoorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.open_door_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OpenDoorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
