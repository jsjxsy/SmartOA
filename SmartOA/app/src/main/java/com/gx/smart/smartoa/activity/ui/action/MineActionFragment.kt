package com.gx.smart.smartoa.activity.ui.action


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gx.smart.smartoa.R

/**
 * A simple [Fragment] subclass.
 */
class MineActionFragment : Fragment() {

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

}
