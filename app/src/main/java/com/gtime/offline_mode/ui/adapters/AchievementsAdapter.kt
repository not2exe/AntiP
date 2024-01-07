package com.gtime.offline_mode.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gtime.general.DiffUtilCallbackImpl
import com.gtime.general.model.dataclasses.Achievement
import com.notexe.gtime.R

class AchievementsAdapter :
    RecyclerView.Adapter<AchievementsHolder>() {
    private var list: List<Achievement> = emptyList()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AchievementsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.achievements_item, parent, false)
        return AchievementsHolder(view)
    }

    override fun onBindViewHolder(
        holder: AchievementsHolder,
        position: Int
    ) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<Achievement>) {
        val diffUtil = DiffUtilCallbackImpl(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }


}