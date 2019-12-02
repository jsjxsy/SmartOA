package com.gx.smart.smartoa.activity.ui.repair

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.repair_fragment.*

class RepairFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = RepairFragment()
    }

    private lateinit var viewModel: RepairViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.repair_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RepairViewModel::class.java)
        initTitle()
        repair_type.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_repairFragment_to_repairTypeFragment)
        }
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.repair)
        }

        right_nav_text_view.let {
            it.setOnClickListener(this)
            it.visibility = View.VISIBLE
            it.text = getString(R.string.repair_record)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.right_nav_text_view -> Navigation.findNavController(v).navigate(R.id.action_repairFragment_to_repairRecordFragment)
        }
    }


}
