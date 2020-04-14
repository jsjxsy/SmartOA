package com.gx.smart.smartoa.activity.ui.visitor.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.activity.ui.visitor.viewmodel.VisitorPassViewModel


/**
 *
 */
class VisitorPassFragment : Fragment() {

    companion object {
        fun newInstance() = VisitorPassFragment()
    }

    private lateinit var viewModel: VisitorPassViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.visitor_pass_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VisitorPassViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
