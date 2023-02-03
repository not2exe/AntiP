package com.gtime.general

import androidx.recyclerview.widget.DiffUtil
import com.gtime.general.model.dataclasses.AppEntity

class DiffUtilAppManager(
    private val oldList: List<AppEntity>,
    private val newList: List<AppEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition] && oldList[oldItemPosition].scores == newList[newItemPosition].scores

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].packageName == newList[newItemPosition].packageName && oldList[oldItemPosition].scores == newList[newItemPosition].scores
}