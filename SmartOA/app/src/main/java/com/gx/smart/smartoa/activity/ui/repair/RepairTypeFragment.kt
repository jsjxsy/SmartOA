package com.gx.smart.smartoa.activity.ui.repair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.repair_type_fragment.*

class RepairTypeFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

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
        initTitle()
        initContent()
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.text = getString(R.string.repair_type)
        }


    }


    private fun initContent() {
        val adapter = RepairTypeAdapter()
        val onItemClick = object : RepairTypeAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val item = adapter.mList!![position]
                val args = Bundle()
                args.putInt(RepairFragment.ARG_TYPE, item.type!!)
                findNavController().navigate(R.id.action_repairTypeFragment_to_repairFragment, args)
            }

        }
        adapter.onItemClick = onItemClick
        recyclerView.adapter = adapter
    }
}
