package com.gtime.online_mode.ui.logic

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.antip.databinding.PromoItemBinding

class PromoViewHolder(view: View) : ViewHolder(view) {
    private val binding = PromoItemBinding.bind(view)
    fun bind(text: String) {
        binding.promoTv.text = text
    }
}
