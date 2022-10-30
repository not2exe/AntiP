package com.gtime

import android.app.AppOpsManager
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.R
import com.example.antip.databinding.FragmentMainBinding
import com.gtime.adapters.AppAdapter
import com.gtime.ui.MainFragmentDirections
import com.gtime.viewmodels.MainFragmentViewModel

class MainViewController(
    private val viewModel: MainFragmentViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val binding: FragmentMainBinding,
    private val navController: NavController,
    private val adapter: AppAdapter,
    private var refreshButtonTimeClick: Long = 0
) {

    fun setUpViews() {
        if (viewModel.getIsLostHardcore()) {
            binding.imageLife.setImageResource(R.drawable.heart_broken)
        }
        if (!checkUsagePerm()) {
            navController
                .navigate(MainFragmentDirections.actionMainFragmentToFirstTimeFragment())
        }
        initRv()
        viewModel.refresh()
        initObservers()
        setupButtons()
    }


    private fun setupButtons() {
        binding.buttonChangeAdapter.setOnClickListener {
            onClickChangeButton()
        }
        binding.buttonSettings.setOnClickListener {
            onClickSettingsButton()
        }
        binding.refreshButton.setOnClickListener {
            onClickRefreshButton(it)
        }
    }


    private fun onClickChangeButton() = with(binding) {
        if (buttonChangeAdapter.isChecked) {
            viewModel.setState(State.USEFUL)
        } else {
            viewModel.setState(State.HARMFUL)
        }
    }

    private fun initRv() = with(binding) {
        mainTable.layoutManager = LinearLayoutManager(context)
        mainTable.adapter = adapter
    }

    private fun onClickRefreshButton(view: View) {
        if (System.currentTimeMillis() > refreshButtonTimeClick + 10000) {
            viewModel.refresh()
            refreshButtonTimeClick = System.currentTimeMillis()
        } else {
            Toast.makeText(context, R.string.wait, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickSettingsButton() {
        navController
            .navigate(MainFragmentDirections.actionMainFragmentToMenuFragment())
    }

    private fun checkUsagePerm(): Boolean {
        return try {
            val appOpsManager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            false
        }
    }

    private fun initObservers() = with(binding) {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            if (viewModel.stateOfKindOfApps.value == State.USEFUL) {
                adapter.updateList(it)
            }
        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            if (viewModel.stateOfKindOfApps.value == State.HARMFUL) {
                adapter.updateList(it)
            }
        }
        viewModel.scoresAll.observe(viewLifecycleOwner) { scoresAll ->
            setupScores(scoresAll)
        }
        viewModel.stateOfKindOfApps.observe(viewLifecycleOwner) { state ->
            setupState(state)
        }
    }

    private fun setupScores(scoresAll: Int) = with(binding) {
        if (scoresAll < 0) {
            scores.setTextColor(Color.RED)
        } else {
            scores.setTextColor(Color.GREEN)
        }
        scores.text = scoresAll.toString()
    }

    private fun setupState(state: State) = with(binding) {
        if (state == State.USEFUL) {
            adapter.updateList(viewModel.usefulApps.value ?: emptyList())
            buttonChangeAdapter.setText(R.string.useful)
        } else {
            adapter.updateList(viewModel.harmfulApps.value ?: emptyList())
            buttonChangeAdapter.setText(R.string.harmful)
        }
    }
}
