package com.gx.smart.smartoa.widget

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.loading_view.*
import top.limuyang2.ldialog.base.BaseLDialog
import top.limuyang2.ldialog.base.ViewHandlerListener

/**
 *@author xiaosy
 *@create 2020-01-13
 *@Describe
 **/
class LoadingDialog : BaseLDialog<LoadingDialog>() {

    override fun layoutView(): View? = null

    override fun viewHandler(): ViewHandlerListener? {
        return null
    }

    override fun layoutRes(): Int {
        return R.layout.loading_view
    }

    /**
     * loading
     */
    fun showLoading(): LoadingDialog {
        iv.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        return this
    }

    /**
     * 成功
     */
    fun showSuccess(): LoadingDialog {
        iv.setImageResource(R.mipmap.load_success)
        iv.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        return this
    }

    /**
     * 失败
     */
    fun showFail(): LoadingDialog {
        iv.setImageResource(R.mipmap.load_fail)
        iv.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        return this
    }

    /**
     * 提示文字
     *
     * @param txt string
     */
    fun setText(txt: String?): LoadingDialog {
        tv.text = txt
        return this
    }

    /**
     * 提示文字
     */
    fun setText(@StringRes txtId: Int): LoadingDialog {
        tv.setText(txtId)
        return this
    }

    companion object {
        fun init(fragmentManager: FragmentManager): LoadingDialog {
            return LoadingDialog().apply { setFragmentManager(fragmentManager) }
        }
    }
}