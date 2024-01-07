package com.gtime.online_mode.ui.logic

import android.content.ClipboardManager
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.gtime.online_mode.data.model.UserPromoModel
import com.notexe.gtime.databinding.PromoCardItemBinding

class PromoCardViewHolder(view: View) : ViewHolder(view) {
    private val binding = PromoCardItemBinding.bind(view)
    fun bind(userPromoModel: UserPromoModel, clipboardManager: ClipboardManager) = with(binding) {
        //TODO Change to FullDescription in future
        fullDescriptionTv.text = userPromoModel.offerModel.description
        Glide.with(icon).load(userPromoModel.offerModel.urlOfferImage).centerCrop().into(icon)
        promoCodes.layoutManager = FlexboxLayoutManager(binding.root.context, FlexDirection.ROW)
        promoCodes.adapter = PromoAdapter(userPromoModel.promoModel.promocodes, clipboardManager)
    }

}
