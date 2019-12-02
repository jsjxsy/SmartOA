package com.gx.smart.smartoa.activity.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.gx.smart.smartoa.R


/**
 * @author xiaosy
 * @create 2019-11-01
 * @Describe
 */
class CompanyAreaPlaceViewBinder :
    ItemViewBinder<CompanyAreaPlace, CompanyAreaPlaceViewBinder.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): ViewHolder {
        val root = inflater.inflate(R.layout.item_mine_company_select_area_place, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, @NonNull companyAreaPlace: CompanyAreaPlace) {
        val args = Bundle()
        args.putString("place", companyAreaPlace.place)
        holder.place.text = companyAreaPlace.place
        holder.itemView.setOnClickListener {
            Navigation.findNavController(holder.itemView)
                .navigate(
                    R.id.action_mineCompanySelectAreaFragment_to_mineCompanySelectCompanyFragment,
                    args
                )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val place: TextView = itemView.findViewById(R.id.place)
    }
}
