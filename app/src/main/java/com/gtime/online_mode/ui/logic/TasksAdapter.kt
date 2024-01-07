package com.gtime.online_mode.ui.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gtime.general.DiffUtilCallbackImpl
import com.gtime.online_mode.ui.TaskUiModel
import com.gtime.online_mode.ui.stateholders.TaskViewModel
import com.notexe.gtime.R

class TasksAdapter(private val viewModel: TaskViewModel) : RecyclerView.Adapter<TaskViewHolder>() {
    var list = emptyList<TaskUiModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.task_item, parent, false
            )
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(list[position], viewModel)

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<TaskUiModel>) {
        val diffUtil = DiffUtilCallbackImpl(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}