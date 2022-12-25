package com.gtime.ui.adapters

import android.content.ClipData
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.ManagerItemBinding
import com.gtime.listeners.DragListener
import com.gtime.model.dataclasses.AppEntity

class ManagerViewHolder(item: View) :
    RecyclerView.ViewHolder(item) {
    private val binding = ManagerItemBinding.bind(item)

    fun bind(
        appEntity: AppEntity,
        dragInstance: DragListener
    ) = with(binding) {
        appIcon.setImageDrawable(appEntity.image)
        appName.text = appEntity.name
        cardManagerLayout.setOnLongClickListener {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(it)
            it?.startDragAndDrop(data, shadowBuilder, it, 0)
            true
        }
        cardManagerLayout.tag = appEntity
        cardManagerLayout.setOnDragListener(dragInstance)
    }
}