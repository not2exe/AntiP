package com.gtime.online_mode.ui.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gtime.general.DiffUtilCallbackImpl
import com.gtime.online_mode.ui.OfferUiModel
import com.gtime.online_mode.ui.stateholders.ShopViewModel
import com.notexe.gtime.R

class ShopAdapter(private val viewModel: ShopViewModel) : RecyclerView.Adapter<ShopViewHolder>() {
    var list = emptyList<OfferUiModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder =
        ShopViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.shop_item, parent, false
            )
        )

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(list[position], viewModel)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<OfferUiModel>) {
        val diffUtil = DiffUtilCallbackImpl(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}