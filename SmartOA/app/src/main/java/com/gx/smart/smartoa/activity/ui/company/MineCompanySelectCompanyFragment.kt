package com.gx.smart.smartoa.activity.ui.company


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.fragment_mine_company_select_company.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MineCompanySelectCompanyFragment : Fragment(), View.OnClickListener {
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
        return inflater.inflate(R.layout.fragment_mine_company_select_company, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTitle()
        initData()
    }

    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title.let {
            it.visibility = View.VISIBLE
            it.text = arguments?.getString("place")
        }

    }

    private fun initData() {

        val adapter = CompanyAdapter()
        recyclerView.adapter = adapter
        adapter.mList = arrayListOf(
            Company("1"),
            Company("2"),
            Company("3")
        )
        val onItemClick =
            object : CompanyAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_mineCompanySelectCompanyFragment_to_mineCompanyEmployeesFragment)
                }

            }

        adapter.onItemClick = onItemClick
    }
}
