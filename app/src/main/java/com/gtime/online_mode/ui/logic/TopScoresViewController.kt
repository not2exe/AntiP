package com.gtime.online_mode.ui.logic

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.databinding.FragmentTopBinding
import com.gtime.online_mode.ui.stateholders.TopScoresViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopScoresViewController(
    private val adapter: TopAdapter,
    private val topBinding: FragmentTopBinding,
    private val viewModel: TopScoresViewModel,
    private val lifecycleScope: CoroutineScope
) {
    fun setupViews() {
        setupRv()
        getScores()
    }

    private fun setupRv() = with(topBinding) {
        topRv.layoutManager = LinearLayoutManager(topBinding.root.context)
        topRv.adapter = adapter
    }

    private fun getScores() {
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

}