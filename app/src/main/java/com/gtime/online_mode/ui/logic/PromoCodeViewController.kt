package com.gtime.online_mode.ui.logic

import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.gtime.online_mode.state_classes.StateOfRequests
import com.gtime.online_mode.ui.stateholders.PromoCodeViewModel
import com.notexe.gtime.R
import com.notexe.gtime.databinding.FragmentPromoCodeBinding

class PromoCodeViewController(
    private val binding: FragmentPromoCodeBinding,
    private val viewModel: PromoCodeViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val adapter: PromoCardAdapter
) {
    fun setupViews() {
        setupObservers()
        binding.promoCardsRv.adapter = adapter
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupObservers() {
        viewModel.usersPromo.observe(viewLifecycleOwner) {
            adapter.updateList(it)
            binding.refreshLayout.isRefreshing = false
        }
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is StateOfRequests.Error.AuthError) {
                Snackbar.make(binding.root, R.string.auth_error, Snackbar.LENGTH_LONG).show()
            }
            viewModel.clearState()
        }
    }
}