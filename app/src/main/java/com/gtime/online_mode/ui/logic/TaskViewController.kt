package com.gtime.online_mode.ui.logic

import androidx.lifecycle.LifecycleOwner
import com.example.antip.R
import com.example.antip.databinding.FragmentTasksBinding
import com.google.android.material.snackbar.Snackbar
import com.gtime.online_mode.state_classes.StateOfRequests
import com.gtime.online_mode.ui.stateholders.TaskViewModel

class TaskViewController(
    private val binding: FragmentTasksBinding,
    private val viewModel: TaskViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val adapter: TasksAdapter
) {
    fun setupViews() {
        setupObservers()
        setupRv()
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupRv() {
        binding.taskRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is StateOfRequests.Error.AuthError) {
                Snackbar.make(binding.root, R.string.auth_error, Snackbar.LENGTH_LONG).show()
            }
            binding.refreshLayout.isRefreshing = false
            viewModel.clearState()
        }
    }
}