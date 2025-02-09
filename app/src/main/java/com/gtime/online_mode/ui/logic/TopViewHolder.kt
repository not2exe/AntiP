package com.gtime.online_mode.ui.logic

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.gtime.general.Constants
import com.gtime.online_mode.data.model.TopScoresModel
import com.notexe.gtime.databinding.TopScoresItemBinding

class TopViewHolder(view: View) : ViewHolder(view) {
    fun bind(item: TopScoresModel, position: Int) {
        val binding = TopScoresItemBinding.bind(itemView)
        binding.apply {
            if (position <= 999) {
                pos.text = position.toString()
            }
            Glide.with(iconIv).load(item.urlAvatar + Constants.AVATAR_URL_68_END)
                .centerCrop().into(iconIv)
            displayName.text = item.userDisplay
            scoresToday.text = item.scores.toString()
        }
    }

}
