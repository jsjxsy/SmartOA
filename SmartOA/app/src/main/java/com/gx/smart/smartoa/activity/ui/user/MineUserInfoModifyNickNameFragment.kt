package com.gx.smart.smartoa.activity.ui.user


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.gx.smart.smartoa.R
import com.gx.smart.smartoa.data.network.api.UserCenterService
import com.gx.smart.smartoa.data.network.api.base.CallBack
import com.gx.wisestone.work.app.grpc.appuser.AppInfoResponse
import kotlinx.android.synthetic.main.fragment_mine_user_info_modify_nick_name.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineUserInfoModifyNickNameFragment : Fragment(), View.OnClickListener {
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
        return inflater.inflate(R.layout.fragment_mine_user_info_modify_nick_name, container, false)
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
            it.text = getString(R.string.modify_nickname)
        }
    }

    private fun initContent() {
        delete.setOnClickListener {
            modifyName.editableText.clear()
        }
        modifyName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                if (length > 0) {
                    save.isEnabled = true
                    delete.visibility = View.VISIBLE
                } else {
                    save.isEnabled = false
                    delete.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        save.setOnClickListener {
            loadingView.visibility = View.VISIBLE
            val name = modifyName.text.toString()
            UserCenterService.getInstance()
                .updateAppUserNickName(name, object : CallBack<AppInfoResponse>() {
                    override fun callBack(result: AppInfoResponse?) {
                        loadingView.visibility = View.GONE
                        if (result == null) {
                            ToastUtils.showLong("修改姓名超时")
                            return
                        }
                        if (result?.code == 100) {
                            ToastUtils.showLong("修改姓名成功")
                            activity?.onBackPressed()
                        } else {
                            ToastUtils.showLong(result?.msg)
                        }
                    }

                })
        }
    }


}
