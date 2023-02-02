package com.gtime.online_mode.ui.logic

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.antip.databinding.TaskItemBinding
import com.gtime.online_mode.ui.StateOfTask
import com.gtime.online_mode.ui.TaskUiModel
import com.gtime.online_mode.ui.stateholders.TaskViewModel

class TaskViewHolder(view: View) : ViewHolder(view) {
    private val binding = TaskItemBinding.bind(view)
    fun bind(taskUiModel: TaskUiModel, viewModel: TaskViewModel) = with(binding) {
        descriptionTv.text = taskUiModel.description
        awardTv.text = taskUiModel.award.toString()
        if (taskUiModel.state == StateOfTask.CLAIMED) {
            claimButton.visibility = View.GONE
            checkbox.visibility = View.VISIBLE
            coinsLayout.visibility = View.GONE
        } else {
            claimButton.visibility = View.VISIBLE
            checkbox.visibility = View.GONE
            coinsLayout.visibility = View.VISIBLE
        }
        claimButton.isEnabled = StateOfTask.READY_TO_CLAIM == taskUiModel.state
        claimButton.setOnClickListener {
            viewModel.onClickClaim(taskUiModel.id, StateOfTask.CLAIMED, taskUiModel.award)
        }
    }
}
