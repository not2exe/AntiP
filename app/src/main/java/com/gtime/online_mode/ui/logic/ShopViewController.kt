package com.gtime.online_mode.ui.logic

import androidx.lifecycle.LifecycleOwner
import com.example.antip.databinding.FragmentShopBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
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
        binding.shopRv.layoutManager = FlexboxLayoutManager(binding.root.context, FlexDirection.ROW)
        binding.shopRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.offersUI.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }
}