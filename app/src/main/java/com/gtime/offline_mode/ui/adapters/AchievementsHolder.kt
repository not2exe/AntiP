package com.gtime.offline_mode.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gtime.general.model.dataclasses.Achievement
import com.notexe.gtime.databinding.AchievementsItemBinding

class AchievementsHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = AchievementsItemBinding.bind(item)
    fun bind(achievement: Achievement) = with(binding) {
        imageAchievement.setImageResource(achievement.imageAchievement)
        descriptionTv.text = achievement.description
        statusAchievement.setImageResource(achievement.imageState)
    }
}