package com.gx.smart.smartoa.activity.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.gx.smart.smartoa.R
import com.gx.smart.lib.base.BaseFragment
import kotlinx.android.synthetic.main.all_features_fragment.*
import kotlinx.android.synthetic.main.layout_common_title.*

class AllFeaturesFragment : BaseFragment(), View.OnClickListener {
    private val adapter = MultiTypeAdapter()
    private val items = ArrayList<Any>()
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.left_nav_image_view -> activity?.onBackPressed()
        }
    }

    companion object {
        fun newInstance() = AllFeaturesFragment()
        const val SPAN_COUNT = 4
    }

    private lateinit var viewModel: AllFeaturesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_features_fragment, container, false)
    }

    override fun onBindLayout(): Int {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllFeaturesViewModel::class.java)
        // TODO: Use the ViewModel
        initTitle()
        initData()
    }


    private fun initTitle() {
        left_nav_image_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
        }
        center_title?.let {
            it.text = getString(R.string.all_feature)
            it.visibility = View.VISIBLE
        }
        right_nav_text_view?.let {
            it.visibility = View.VISIBLE
            it.setOnClickListener(this)
            it.text = getString(R.string.edit)
        }

    }

    private fun initData() {
        adapter.register(CategoryViewBinder())
        val featureViewBinder = FeatureViewBinder()
        featureViewBinder.fragmentManager = fragmentManager!!
        adapter.register(featureViewBinder)
        adapter.register(DividerViewBinder())

        val layoutManager = GridLayoutManager(activity, SPAN_COUNT)
        val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = items[position]
                return if (item is Category || item is Divider) SPAN_COUNT else 1
            }
        }
        layoutManager.spanSizeLookup = spanSizeLookup
        featureRecyclerView.layoutManager = layoutManager

        featureRecyclerView.adapter = adapter
        val space = resources.getDimensionPixelSize(R.dimen.divider_height_style)
        featureRecyclerView.addItemDecoration(PostItemDecoration(space, spanSizeLookup))

        val category1 = Category(getString(R.string.my_feature))
        items.add(category1)
        val item1 =
            Feature(R.drawable.ic_environmental_control, getString(R.string.environmental_control))
//        val item2 =
//            Feature(R.drawable.ic_meeting_schedule, getString(R.string.meeting_schedule))
        val item3 =
            Feature(R.drawable.ic_attendance, getString(R.string.attendance))
        val item4 =
            Feature(R.drawable.ic_repair, getString(R.string.repair))
//        val item5 =
//            Feature(R.drawable.ic_air_quality, getString(R.string.air_quality))
//        val item6 =
//            Feature(R.drawable.ic_share_work, getString(R.string.share_work))
//        val item7 =
//            Feature(R.drawable.ic_visitor, getString(R.string.visitor))
        items.add(item1)
//        items.add(item2)
        items.add(item3)
        items.add(item4)
//        items.add(item5)
//        items.add(item6)
//        items.add(item7)

        items.add(Divider())

        val category21 = Category(getString(R.string.all_feature))


        items.add(category21)
        val item11 =
            Feature(R.drawable.ic_environmental_control, getString(R.string.environmental_control))
//        val item12 =
//            Feature(R.drawable.ic_meeting_schedule, getString(R.string.meeting_schedule))
        val item13 =
            Feature(R.drawable.ic_attendance, getString(R.string.attendance))
        val item14 =
            Feature(R.drawable.ic_repair, getString(R.string.repair))
//        val item15 =
//            Feature(R.drawable.ic_air_quality, getString(R.string.air_quality))
//        val item16 =
//            Feature(R.drawable.ic_share_work, getString(R.string.share_work))
//        val item17 =
//            Feature(R.drawable.ic_visitor, getString(R.string.visitor))
        items.add(item11)
//        items.add(item12)
        items.add(item13)
        items.add(item14)
//        items.add(item15)
//        items.add(item16)
//        items.add(item17)
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

}
