package com.example.antip.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.AchievementsItemBinding
import com.example.antip.model.dataclasses.Achievement

class AchievementsAdapter(private var list: ArrayList<Achievement>) :
    RecyclerView.Adapter<AchievementsAdapter.AchievementsHolder>() {


    class AchievementsHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = AchievementsItemBinding.bind(item)
        fun bind(achievement: Achievement) = with(binding) {
            imageAchievement.setImageResource(achievement.imageAchievement)
            descriptionTv.text = achievement.description
            statusAchievement.setImageResource(achievement.imageState)


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.achievements_item, parent, false)
        return AchievementsHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementsHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: ArrayList<Achievement>) {
        this.list = list
        notifyDataSetChanged()
    }


}