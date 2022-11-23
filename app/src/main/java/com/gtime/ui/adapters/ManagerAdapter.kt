package com.gtime.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.gtime.Constants
import com.gtime.KindOfApps
import com.gtime.listeners.DragListener
import com.gtime.model.dataclasses.AppEntity


class ManagerAdapter(val dragListener: DragListener, context: Context) :
    RecyclerView.Adapter<ManagerViewHolder>() {
    var list: List<AppEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerViewHolder =
        ManagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.manager_item, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ManagerViewHolder, position: Int) {
        holder.bind(list[position],dragListener)
    }

    fun updateList(list: List<AppEntity>) {
        val diffUtil = DiffUtilCallbackImpl(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }

}