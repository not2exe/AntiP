package com.gtime.online_mode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.antip.R

class TopAdapter : PagingDataAdapter<TopScoresModel, TopViewHolder>(Companion) {
    override fun onBindViewHolder(holder: TopViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return,position+1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopViewHolder =
        TopViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.top_scores_item, parent, false
            )
        )

    companion object : DiffUtil.ItemCallback<TopScoresModel>() {
        override fun areItemsTheSame(oldItem: TopScoresModel, newItem: TopScoresModel): Boolean =
            oldItem.documentID == newItem.documentID

        override fun areContentsTheSame(oldItem: TopScoresModel, newItem: TopScoresModel): Boolean =
            oldItem == newItem

    }
}