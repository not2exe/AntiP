package com.antip.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.antip.adapters.AchievementsAdapter
import com.example.antip.databinding.FragmentAchievementsBinding
import com.example.antip.model.dataclasses.Achievement
import com.example.antip.viewmodels.AchievementsFragmentViewModel


class AchievementsFragment : Fragment(R.layout.fragment_achievements) {
    private var bindingOrNull: FragmentAchievementsBinding? =
        null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private val achievementsList: ArrayList<Achievement> = ArrayList()

    private val viewModel by viewModels<AchievementsFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindingOrNull = FragmentAchievementsBinding.bind(view)
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(AchievementsFragmentDirections.actionStatsFragmentToMenuFragment())
        }
        binding.achievementsTable.init(achievementsList)



        viewModel.initAchievements()
        initObservers()




        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservers() {
        viewModel.achievements.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> achievementsList.add(it1) }
            }
            (binding.achievementsTable.adapter as _root_ide_package_.com.antip.adapters.AchievementsAdapter).updateList(achievementsList)
        }


    }

    private fun RecyclerView.init(list: ArrayList<Achievement>) {
        this.layoutManager = LinearLayoutManager(context)
        val adapter = _root_ide_package_.com.antip.adapters.AchievementsAdapter(list)
        this.adapter = adapter
    }


}