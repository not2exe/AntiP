package com.gtime.online_mode.ui.logic

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.example.antip.R
import com.example.antip.databinding.FragmentShopBinding
import com.google.android.material.snackbar.Snackbar
import com.gtime.online_mode.state_sealed_class.StateOfBuy
import com.gtime.online_mode.ui.stateholders.ShopViewModel

class ShopViewController(
    private val binding: FragmentShopBinding,
    private val viewModel: ShopViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val adapter: ShopAdapter
) {
    fun setupViews() {
        setupRv()
        setupObservers()
    }

    private fun setupRv() {
        binding.shopRv.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.shopRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.offersUI.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
        viewModel.stateCoinRep.observe(viewLifecycleOwner) {
            if (it is StateOfBuy.SuccessWithCoins) {
                viewModel.getPromoCodes(it.offerID, it.cost)
            }
            if (it is StateOfBuy.LackOfCoinsError) {
                showSnackBar(R.string.lack_of_coins)
            }
            viewModel.clearCoinState()
        }
        viewModel.statePromoRep.observe(viewLifecycleOwner) {
            if (it is StateOfBuy.PromoError) {
                viewModel.moneyBack(it.cost)
                showSnackBar(R.string.error_order)
            }
            if (it is StateOfBuy.FullSuccess) {
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