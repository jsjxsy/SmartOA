package com.gx.smart.repair

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
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
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.repair_type)
        }

    }


    private fun initContent() {
        val adapter = RepairTypeAdapter()
        val onItemClick = object : RepairTypeAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val item = adapter.mList!![position]
                val intent  = Intent()
                intent.putExtra(RepairFragment.ARG_TYPE, item)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }

        }
        adapter.onItemClick = onItemClick
        adapter.mList = arrayListOf(
            RepairType(1,"设备损坏"),
            RepairType(2,"办公绿化"),
            RepairType(3,"公共卫生")
        )

        recyclerView.adapter = adapter
        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}
