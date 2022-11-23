package com.gtime.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.databinding.AchievementsItemBinding
import com.gtime.model.dataclasses.Achievement

class AchievementsHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = AchievementsItemBinding.bind(item)
    fun bind(achievement: Achievement) = with(binding) {
        imageAchievement.setImageResource(achievement.imageAchievement)
        descriptionTv.text = achievement.description
        statusAchievement.setImageResource(achievement.imageState)
    }
}