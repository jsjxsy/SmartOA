package com.gx.smart.smartoa.activity.ui.visitor

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class VisitorFragment : Fragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance() = VisitorFragment()
    }

    private lateinit var viewModel: VisitorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.visitor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VisitorViewModel::class.java)
        // TODO: Use the ViewModel
        initTitle()
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.visitor)
        }
        right_nav_text_view.let {
            it.visibility = View.VISIBLE
            it.text= getString(R.string.visit_record)
        }
    }

}
