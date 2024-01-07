package com.gtime.online_mode.ui.logic

import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gtime.general.Constants
import com.notexe.gtime.R
import com.notexe.gtime.databinding.PromoItemBinding


class PromoViewHolder(view: View) : ViewHolder(view) {
    private val binding = PromoItemBinding.bind(view)
    fun bind(text: String, clipboardManager: ClipboardManager) = with(binding) {
        promoLayout.setOnLongClickListener {
            val clip = ClipData.newPlainText(Constants.LABEL, text)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(itemView.context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT)
                .show()
            promoTv.visibility = View.VISIBLE
            true
        }
        promoLayout.setOnClickListener {
            promoTv.visibility = View.INVISIBLE
        }
        promoTv.visibility = View.INVISIBLE
        promoTv.text = text
    }
}
