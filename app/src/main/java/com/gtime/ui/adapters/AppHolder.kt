package com.gtime.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.databinding.AppItemBinding
import com.gtime.model.dataclasses.AppEntity

class AppHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = AppItemBinding.bind(item)
    fun bind(appEntity: AppEntity) = with(binding) {
        if (appEntity.image == null) {
            //TODO Заглушку
        } else {
            imageView.setImageDrawable(appEntity.image)
        }
        name.text = appEntity.name
        scores.text = appEntity.scores.toString()
    }
}
