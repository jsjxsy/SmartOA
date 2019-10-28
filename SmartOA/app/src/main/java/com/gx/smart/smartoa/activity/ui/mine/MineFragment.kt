package com.gx.smart.smartoa.activity.ui.mine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment() {

    companion object {
        fun newInstance() = MineFragment()
    }

    private lateinit var viewModel: MineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MineViewModel::class.java)
        // TODO: Use the ViewModel
        val mineList = mine_setting_list_view as ListView
        val adapter = MineAdapter(activity as Context)
        adapter.lists = arrayListOf(
            Item(
                R.drawable.ic_mine_company, getString(R.string.mine_company)
            ),
            Item(
                R.drawable.ic_mine_meeting, getString(R.string.mine_meeting)
            ),
            Item(
                R.drawable.ic_mine_action, getString(R.string.mine_action)
            ),
            Item(
                R.drawable.ic_mine_visitor, getString(R.string.mine_visitor)
            )
        )
        mineList.adapter = adapter
    }

    class Item {
        var itemIcon: Int
        var itemName: String

        constructor(itemIcon: Int, itemName: String) {
            this.itemIcon = itemIcon
            this.itemName = itemName
        }
    }

}
