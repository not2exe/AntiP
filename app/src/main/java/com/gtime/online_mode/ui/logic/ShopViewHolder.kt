package com.gtime.online_mode.ui.logic

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.antip.R
import com.example.antip.databinding.ShopItemBinding
import com.gtime.online_mode.domain.OfferUiModel

class ShopViewHolder(view: View) : ViewHolder(view) {
    private val binding = ShopItemBinding.bind(view)
    fun bind(offerUiModel: OfferUiModel) = with(binding) {
        costTv.text = offerUiModel.cost.toString()
        Glide.with(icon).load(offerUiModel.urlOfferImage)
            .centerCrop().into(icon)
        descriptionTv.text = offerUiModel.description
        buyButton.setBackgroundColor(R.drawable.button_background)
        buyButton.isEnabled = offerUiModel.isAvailable
    }

}
