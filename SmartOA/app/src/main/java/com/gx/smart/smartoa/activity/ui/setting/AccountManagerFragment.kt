package com.gx.smart.smartoa.activity.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.fragment_account_manager.*
import kotlinx.android.synthetic.main.layout_common_title.*


class AccountManagerFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_manager, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            it.text = getString(R.string.account_manage)
        }
    }

    private fun initContent() {
        accountUnregisterLayout.setOnClickListener(this)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            AccountManagerFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
            R.id.accountUnregisterLayout -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_settingFragment_to_accountManagerFragment)
            }
        }
    }
}
