package com.gtime.adapters

import android.content.ClipData
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.databinding.AppItemSettingsBinding
import com.gtime.listeners.DragListener
import com.gtime.model.dataclasses.AppEntity

class ManagerViewHolder(item: View) :
    RecyclerView.ViewHolder(item) {
    private val binding = AppItemSettingsBinding.bind(item)
    fun bind(
        appEntity: AppEntity,
        position: Int,
        dragInstance: DragListener
    ) = with(binding) {
        imageViewSettings.setImageDrawable(appEntity.image)
        nameSettings.text = appEntity.name
        frameLayoutSettings.tag = position
        frameLayoutSettings.setOnLongClickListener {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(it)
            it?.startDragAndDrop(data, shadowBuilder, it, 0)
            true
        }
        frameLayoutSettings.setOnDragListener(dragInstance)
    }
}