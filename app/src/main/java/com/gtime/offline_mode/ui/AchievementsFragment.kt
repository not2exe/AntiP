package com.gtime.offline_mode.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.FragmentAchievementsBinding
import com.gtime.general.app.App
import com.gtime.general.LambdaFactory
import com.gtime.offline_mode.ui.adapters.AchievementsAdapter
import com.gtime.offline_mode.ui.stateholders.AchievementsFragmentViewModel
import javax.inject.Inject


class AchievementsFragment : Fragment(R.layout.fragment_achievements) {

    @Inject
    lateinit var factory: AchievementsFragmentViewModel.Factory

    private val viewModel: AchievementsFragmentViewModel by viewModels {
        LambdaFactory(this) { stateHandle ->
            factory.create(
                stateHandle
            )
        }
    }

    private val adapterAchievement by lazy { AchievementsAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireContext().applicationContext as App).appComponent.activity().create(requireActivity()).inject(this)
        FragmentAchievementsBinding.bind(view).apply {
            achievementsTable.init()
        }
        viewModel.initAchievements()
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservers() {
        viewModel.achievements.observe(viewLifecycleOwner) {
            adapterAchievement.updateList(it)
        }
    }

    private fun RecyclerView.init() {
        this.layoutManager = LinearLayoutManager(context)
        this.adapter = adapterAchievement
    }


}