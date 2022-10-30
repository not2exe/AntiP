package com.gtime.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.gtime.Constants
import com.gtime.listeners.DragListener
import com.gtime.model.dataclasses.AppEntity


class ManagerAdapter(val dragListener: DragListener, context: Context) :
    RecyclerView.Adapter<ManagerViewHolder>() {
    var list: List<AppEntity> = emptyList()
    private val undefined: AppEntity =
        AppEntity(
            ContextCompat.getDrawable(context, R.drawable.undefined)!!,
            Constants.EMPTY,
            0
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerViewHolder =
        ManagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.app_item_settings, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ManagerViewHolder, position: Int) {
        holder.bind(list[position], position, dragListener)
    }

    fun updateList(list: List<AppEntity>) {
        var tempList =
            list.ifEmpty { listOf(undefined) }
        if (tempList.size > 1)tempList= tempList.filter { it != undefined }

        val diffUtil = DiffUtilCallbackImpl(this.list, tempList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = tempList
        diffResult.dispatchUpdatesTo(this)
    }

}