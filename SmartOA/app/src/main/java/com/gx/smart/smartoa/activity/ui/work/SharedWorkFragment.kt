package com.gx.smart.smartoa.activity.ui.work

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class SharedWorkFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = SharedWorkFragment()
    }

    private lateinit var viewModel: SharedWorkViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shared_work_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SharedWorkViewModel::class.java)
        initTitle()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.share_work)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

}
