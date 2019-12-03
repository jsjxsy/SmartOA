package com.gx.smart.smartoa.activity.ui.action


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.list_action_layout.*

/**
 * A simple [Fragment] subclass.
 */
class MineActionFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MineActionFragment()
        const val ACTION_TYPE = 3
    }

    private lateinit var viewModel: ActionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine_action, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActionViewModel::class.java)
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
            it.text = getString(R.string.mine_action)
        }
    }

    private fun initContent() {
        val adapter = ActionAdapter()
        adapter.mList = arrayListOf(Action("", ""))
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {

    }

}
