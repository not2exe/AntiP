package com.gtime.online_mode.ui.logic

import android.content.ClipboardManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notexe.gtime.R

class PromoAdapter(private val list: List<String>, private val clipboardManager: ClipboardManager) :
    RecyclerView.Adapter<PromoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder =
        PromoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.promo_item, parent, false)
        )

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) =
        holder.bind(list[position],clipboardManager)

    override fun getItemCount(): Int = list.size
}
