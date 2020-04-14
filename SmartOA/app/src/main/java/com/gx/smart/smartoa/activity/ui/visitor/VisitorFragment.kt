package com.gx.smart.smartoa.activity.ui.visitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class VisitorFragment : BaseFragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.right_nav_text_view -> Navigation.findNavController(v).navigate(R.id.action_visitorFragment_to_mineVisitorFragment)
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

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VisitorViewModel::class.java)
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
            it.setOnClickListener(this)
        }
    }

}
