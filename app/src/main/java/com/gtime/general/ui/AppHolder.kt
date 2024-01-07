package com.gtime.general.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gtime.general.model.dataclasses.AppEntity
import com.notexe.gtime.databinding.AppItemBinding

class AppHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = AppItemBinding.bind(item)
    fun bind(appEntity: AppEntity) = with(binding) {
        if (appEntity.image != null) {
            imageView.setImageDrawable(appEntity.image)
        }
        name.text = appEntity.name
        scores.text = appEntity.scores.toString()
    }
}
