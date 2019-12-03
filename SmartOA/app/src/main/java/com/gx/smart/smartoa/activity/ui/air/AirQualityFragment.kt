package com.gx.smart.smartoa.activity.ui.air

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.air_quality_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class AirQualityFragment : Fragment(), View.OnClickListener {

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
        initTitle()
        initContent()
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.air_quality)
        }
    }

    private fun initContent() {
        val adapter = AirQualityAdapter()
        adapter.mList = arrayListOf(
            AirQuality("", ""),
            AirQuality("", "")
        )
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }
}
