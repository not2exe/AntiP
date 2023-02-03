package com.gtime.offline_mode.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.databinding.FragmentAppManagerBinding
import com.gtime.offline_mode.ui.adapters.ManagerAdapter
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel

class AppManagerViewController(
    private val usefulAdapter: ManagerAdapter,
    private val harmfulAdapter: ManagerAdapter,
    private val othersAdapter: ManagerAdapter,
    private val adapterPredicts: ArrayAdapter<String>,
    private val viewModel: AppManagerFragmentViewModel,
    private val binding: FragmentAppManagerBinding,
    private val viewLifecycleOwner: LifecycleOwner
) {
    fun setupViews() {
        initRvs()
        initObservers()
        initAutocomplete()
    }

    private fun initAutocomplete() = with(binding) {
        autoCompleteTV.setAdapter(adapterPredicts)
        autoCompleteTV.threshold = 1
        autoCompleteTV.setOnFocusChangeListener { _, focus ->
            if (focus) {
                toxicLayout.visibility = View.GONE
                usefulLayout.visibility = View.GONE
            } else {
                toxicLayout.visibility = View.VISIBLE
                usefulLayout.visibility = View.VISIBLE
            }
        }
        autoCompleteTV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val imm =
                root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(autoCompleteTV.windowToken, 0)
            autoCompleteTV.clearFocus()
            rvOthers.scrollToPosition(viewModel.getPos(adapterPredicts.getItem(position)))
        }
    }


    private fun initObservers() = with(binding) {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            usefulAdapter.updateList(it)
        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            harmfulAdapter.updateList(it)
        }
        viewModel.neutralApps.observe(viewLifecycleOwner) { list ->
            othersAdapter.updateList(list)
            viewModel.providePredictsAndPos(provideHashMapNamesAndPos())
            adapterPredicts.clear()
            adapterPredicts.addAll(list.map { it.name })
        }
    }

    private fun initRvs() = with(binding) {
        rvHarmful.init(harmfulAdapter)
        rvUseful.init(usefulAdapter)
        rvOthers.init(othersAdapter)
    }

    private fun RecyclerView.init(adapter: ManagerAdapter) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.adapter = adapter
        this.setOnDragListener(adapter.dragListener)
    }

    private fun provideHashMapNamesAndPos(): HashMap<String, Int> = with(binding) {
        val hashMap = HashMap<String, Int>()
        for (i in othersAdapter.list.indices) {
            hashMap[othersAdapter.list[i].name] = i
        }
        return@with hashMap
    }
}
