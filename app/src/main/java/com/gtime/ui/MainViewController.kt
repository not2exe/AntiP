package com.gtime.ui

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.R
import com.example.antip.databinding.FragmentMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.gtime.State
import com.gtime.adapters.AppAdapter
import com.gtime.ui.stateholders.MainFragmentViewModel

class MainViewController(
    private val viewModel: MainFragmentViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val binding: FragmentMainBinding,
    private val navController: NavController,
    private val adapter: AppAdapter,
) {

    fun setUpViews() {
        if (!checkUsagePerm()) {
            showBottomSheet()
        }
        initRv()
        viewModel.refresh()
        initObservers()
        setupButtons()
    }

    private fun showBottomSheet() {
        val bottomSheetDialogFragment = BottomSheetDialog(context).apply {
            setContentView(R.layout.fragment_first_time)
            findViewById<MaterialButton>(R.id.open_settings_button)
                ?.setOnClickListener { context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }
            setCancelable(false)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            show()
        }

    }


    private fun setupButtons() {
        binding.buttonChangeAdapter.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setState(State.HARMFUL)
            } else {
                viewModel.setState(State.USEFUL)
            }
        }
        binding.buttonSettings.setOnClickListener {
            onClickSettingsButton()
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }


    private fun initRv() = with(binding) {
        mainTable.layoutManager = LinearLayoutManager(context)
        mainTable.adapter = adapter
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
            refreshLayout.isRefreshing = false
            if (viewModel.stateOfKindOfApps.value == State.USEFUL) {
                adapter.updateList(it)
            }
        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            refreshLayout.isRefreshing = false
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
