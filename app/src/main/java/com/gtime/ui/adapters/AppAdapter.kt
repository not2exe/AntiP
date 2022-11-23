package com.gtime.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gtime.model.dataclasses.AppEntity
import com.example.antip.R

class AppAdapter : RecyclerView.Adapter<AppHolder>() {
    private var list = emptyList<AppEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)
        return AppHolder(view)
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<AppEntity>) {
        val diffUtil = DiffUtilCallbackImpl(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}
