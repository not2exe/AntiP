package com.gtime.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallbackImpl(
    private val oldList: List<Entity>,
    private val newList: List<Entity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].packageName == newList[newItemPosition].packageName
}