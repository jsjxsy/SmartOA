package com.gx.smart.smartoa.activity.ui.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.gx.smart.smartoa.R
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_mine_user_info.*
import kotlinx.android.synthetic.main.layout_common_title.*
import top.limuyang2.customldialog.BottomTextListDialog
import top.limuyang2.customldialog.adapter.BottomTextListAdapter

/**
 * A simple [Fragment] subclass.
 */
class MineUserInfoFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_user_info, container, false)
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
            it.text = getString(R.string.my_info)
        }
    }

    private fun initContent() {
        Glide.with(this).load(R.mipmap.home_banner_test).apply(
            RequestOptions.bitmapTransform(
                CircleCrop()
            )
        ).into(headImage)

        phoneLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mineUserInfoFragment_to_mineUserInfoModifyNameFragment)
        }
        sexLayout.setOnClickListener {
            val dialog = BottomTextListDialog.init(fragmentManager!!)
            dialog.setTextList(listOf("男", "女"))
                .setOnItemClickListener(object : BottomTextListAdapter.OnItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        when (position) {
                            0 -> {
                                Logger.i("hello")
                                dialog.dismiss()
                            }
                            1 -> {
                                Logger.i("hello")
                                dialog.dismiss()
                            }

                        }
                    }

                }).show()
        }
        modifyPhoneLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mineUserInfoFragment_to_mineUserInfoModifyPhoneFragment)
        }

        causeLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mineUserInfoFragment_to_mineVerifyFragment)
        }

        modifyPasswordLayout.setOnClickListener {
            findNavController().navigate(R.id.action_mineUserInfoFragment_to_mineUserInfoModifyPasswordFragment)
        }

    }
}
