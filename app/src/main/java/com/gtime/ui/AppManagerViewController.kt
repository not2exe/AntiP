package com.gtime.ui

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.databinding.FragmentAppManagerBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.gtime.ui.adapters.ManagerAdapter
import com.gtime.ui.stateholders.AppManagerFragmentViewModel

class AppManagerViewController(
    private val usefulAdapter: ManagerAdapter,
    private val harmfulAdapter: ManagerAdapter,
    private val othersAdapter: ManagerAdapter,
    private val navController: NavController,
    private val viewModel: AppManagerFragmentViewModel,
    private val binding: FragmentAppManagerBinding,
    private val viewLifecycleOwner: LifecycleOwner
) {
    fun setupViews() {
        initRvs()
        initButtons()
        initObservers()
    }


    private fun initObservers() = with(binding) {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            usefulAdapter.updateList(it)
        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            harmfulAdapter.updateList(it)
        }
        viewModel.neutralApps.observe(viewLifecycleOwner) {
            othersAdapter.updateList(it)
        }
    }

    private fun initRvs() = with(binding) {
        rvHarmful.init(harmfulAdapter)
        rvUseful.init(usefulAdapter)
        rvOthers.init(othersAdapter)
    }

    private fun initButtons() = with(binding) {
        buttonBack.setOnClickListener {
            navController.navigate(AppManagerFragmentDirections.actionAppManagerFragmentToMenuFragment())
        }
    }

    private fun RecyclerView.init(adapter: ManagerAdapter) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        this.adapter = adapter
        this.setOnDragListener(adapter.dragListener)
    }
}
