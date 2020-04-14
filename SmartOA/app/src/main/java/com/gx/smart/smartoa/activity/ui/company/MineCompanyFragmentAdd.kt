package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.fragment_mine_company_add.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanyFragmentAdd : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addCompany -> Navigation.findNavController(v).navigate(R.id.action_mineCompanyFragment_to_mineCompanySelectAreaFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_company_add, container, false)

    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addCompany.setOnClickListener(this)
    }


}
