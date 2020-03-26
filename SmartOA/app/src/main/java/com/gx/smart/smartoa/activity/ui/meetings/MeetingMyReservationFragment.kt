package com.gx.smart.smartoa.activity.ui.meetings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gx.smart.lib.base.BaseFragment
import com.gx.smart.smartoa.R
import kotlinx.android.synthetic.main.fragment_meeting_my_reservation.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * A simple [Fragment] subclass.
 */
class MeetingMyReservationFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_my_reservation, container, false)
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
        center_title?.let {
            it.text = getString(R.string.mine_schedule)
            it.visibility = View.VISIBLE
        }
    }

    private fun initContent() {
        val adapter = ReservationAdapter()
        recyclerView.adapter = adapter
        val list = listOf(Reservation("", ""))
        adapter.setList(list)
        val onItemClickListener = object : ReservationAdapter.OnItemClickListener{}
        adapter.onItemClick = onItemClickListener

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }
}
