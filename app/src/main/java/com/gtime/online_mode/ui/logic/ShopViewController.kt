package com.gtime.online_mode.ui.logic

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gtime.online_mode.state_classes.StateOfRequests
import com.gtime.online_mode.ui.stateholders.ShopViewModel
import com.notexe.gtime.R
import com.notexe.gtime.databinding.FragmentShopBinding

class ShopViewController(
    private val binding: FragmentShopBinding,
    private val viewModel: ShopViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val adapter: ShopAdapter
) {
    fun setupViews() {
        setupRv()
        setupObservers()
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refreshOffers()
        }
    }

    private fun setupRv() {
        binding.shopRv.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.shopRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.offersUI.observe(viewLifecycleOwner) {
            adapter.updateList(it)
            binding.refreshLayout.isRefreshing = false
        }
        viewModel.stateCoinRep.observe(viewLifecycleOwner) {
            if (it is StateOfRequests.Success.SuccessWithCoins) {
                viewModel.getPromoCodes(it.offerID, it.cost)
            }
            if (it is StateOfRequests.Error.AuthError) {
                showSnackBar(R.string.auth_error)
            }
            if (it is StateOfRequests.Error.LackOfCoinsError) {
                showSnackBar(R.string.lack_of_coins)
            }
            viewModel.clearCoinState()
        }
        viewModel.statePromoRep.observe(viewLifecycleOwner) {
            if (it is StateOfRequests.Error.PromoError) {
                viewModel.moneyBack(it.cost)
                showSnackBar(R.string.error_order)
            }
            if (it is StateOfRequests.Success.FullSuccess) {
                showSnackBar(R.string.sucess_order)
            }
            viewModel.clearPromoState()
        }
    }

    private fun showSnackBar(id: Int) {
        Snackbar.make(
            binding.root,
            id,
            Snackbar.LENGTH_LONG
        ).show()
    }

}