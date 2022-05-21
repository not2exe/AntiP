package com.example.antip.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.adapters.AppAdapter
import com.example.antip.adapters.StatsAdapter
import com.example.antip.databinding.FragmentStatsBinding
import com.example.antip.model.db.DailyStatsEntry
import com.example.antip.viewmodels.StatsFragmentViewModel


class StatsFragment : Fragment(R.layout.fragment_stats) {
    private var bindingOrNull: FragmentStatsBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private val statsList: ArrayList<DailyStatsEntry> = ArrayList<DailyStatsEntry>()

    private val viewModel by viewModels<StatsFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindingOrNull = FragmentStatsBinding.bind(view)
        val mySet = linkedMapOf("label1" to 4F,"label2" to 7F,"label3" to 2F)
        binding.tableStats.init(statsList)



        viewModel.initStats()
        initObservers()




        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservers() {
        viewModel.stats.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> statsList.add(it1) }
            }
            Log.d("StatsFragment",it.toString())
            (binding.tableStats.adapter as StatsAdapter).updateList(statsList)
        }



    }

    private fun RecyclerView.init(list: ArrayList<DailyStatsEntry>) {
        this.layoutManager = LinearLayoutManager(context)
        val adapter = StatsAdapter(list)
        this.adapter = adapter
    }

}