package com.gx.smart.smartoa.activity.ui.login.password

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*

class ForgetPasswordFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() =
            ForgetPasswordFragment()
    }

    private lateinit var viewModel: ForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = Color.WHITE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forget_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgetPasswordViewModel::class.java)
        initTitle()
    }

    private fun initTitle() {
        left_nav_image_view.visibility = View.VISIBLE
        center_title?.let {
            it.visibility = View.VISIBLE
            it.text = getString(R.string.forget_password)
        }
        left_nav_image_view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }
}
