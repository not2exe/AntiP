package com.gtime.online_mode.ui.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.gtime.general.DiffUtilCallbackImpl
import com.gtime.online_mode.data.model.UserPromoModel

class PromoCardAdapter : RecyclerView.Adapter<PromoCardViewHolder>() {
    var list = emptyList<UserPromoModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoCardViewHolder =
        PromoCardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.promo_card_item, parent, false
            )
        )

    override fun onBindViewHolder(holder: PromoCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<UserPromoModel>) {
        val diffUtil = DiffUtilCallbackImpl(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}
