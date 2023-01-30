package com.gtime.online_mode

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.antip.databinding.TopScoresItemBinding
import com.gtime.general.Constants

class TopViewHolder(view: View) : ViewHolder(view) {
    fun bind(item: TopScoresModel) {
        val binding = TopScoresItemBinding.bind(itemView)
        binding.apply {
            Glide.with(iconIv).load(item.urlAvatar + Constants.AVATAR_URL_68_END)
                .centerCrop().into(iconIv)
            displayName.text = item.userDisplay
            scoresToday.text = item.scores.toString()
        }
    }

}
