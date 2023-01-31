package com.gtime.general.ui

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.R
import com.example.antip.databinding.FragmentMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.gtime.general.KindOfApps
import com.gtime.offline_mode.AnimateState
import com.gtime.offline_mode.ui.stateholders.MainFragmentViewModel
import kotlinx.coroutines.launch


class MainViewController(
    private val viewModel: MainFragmentViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val binding: FragmentMainBinding,
    private val adapter: AppAdapter,
    private val bottomSheetDialogFragment: BottomSheetDialog = BottomSheetDialog(context),
    private var firstHeartState: AnimateState = AnimateState.PULSATE_FIRST,
    private var secondHearState: AnimateState = AnimateState.PULSATE_FIRST,
    private var thirdHeartState: AnimateState = AnimateState.PULSATE_FIRST
) {

    fun setUpViews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (!checkUsagePerm()) {
                    showBottomSheet()
                }
            }
        }
        initRv()
        initObservers()
        setupButtons()
    }

    private fun showBottomSheet() {
        bottomSheetDialogFragment.apply {
            setContentView(R.layout.fragment_first_time)
            findViewById<MaterialButton>(R.id.open_settings_button)
                ?.setOnClickListener {
                    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    dismiss()
                }
            setCancelable(false)
            show()
        }
    }


    private fun setupPulsateAnim(
        view: ShapeableImageView,
        stateHeart: AnimateState,
        isBroken: Boolean,
    ) {
        var animateState = stateHeart
        var duration: Int = context.resources.getInteger(R.integer.heart_beat_duration)
        var scaleX = getFloat(R.dimen.heart_expand)
        var scaleY = getFloat(R.dimen.heart_expand)
        when (stateHeart) {
            AnimateState.PULSATE_FIRST -> {
                animateState = AnimateState.PULSATE_SECOND
            }
            AnimateState.PULSATE_SECOND -> {
                scaleX = getFloat(R.dimen.heart_pre_expand)
                scaleY = getFloat(R.dimen.heart_pre_expand)
                animateState = AnimateState.PULSATE_THIRD
            }
            AnimateState.PULSATE_THIRD -> {
                animateState = AnimateState.END_PULSATE
            }
            AnimateState.END_PULSATE -> {
                duration = context.resources.getInteger(R.integer.heart_calm_duration)
                scaleX = getFloat(R.dimen.heart_calm)
                scaleY = getFloat(R.dimen.heart_calm)
                animateState = AnimateState.END_ANIM
            }
            AnimateState.END_ANIM -> {
                if (isBroken) {
                    view.setImageResource(R.drawable.ic_heart_broken_48)
                    return
                }
                animateState = AnimateState.PULSATE_SECOND
            }
        }
        view.animate().scaleX(scaleX).scaleY(scaleY)
            .withEndAction { setupPulsateAnim(view, animateState, isBroken) }
            .setDuration(duration.toLong())
            .start()
    }

    private fun getFloat(id: Int, typedValue: TypedValue = TypedValue()): Float {
        context.resources.getValue(id, typedValue, false)
        return typedValue.float
    }


    private fun setupButtons() {
        binding.buttonChangeAdapter.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setState(KindOfApps.TOXIC)
            } else {
                viewModel.setState(KindOfApps.USEFUL)
            }
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }


    private fun initRv() = with(binding) {
        mainTable.layoutManager = LinearLayoutManager(context)
        mainTable.adapter = adapter
    }


    private fun checkUsagePerm(): Boolean {
        return try {
            val appOpsManager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOpsManager.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.packageName
                )
            } else {
                appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            false
        }
    }

    private fun initObservers() = with(binding) {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            if (viewModel.stateOfKindOfApps.value == KindOfApps.USEFUL) {
                adapter.updateList(it)
            }
        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            if (viewModel.stateOfKindOfApps.value == KindOfApps.TOXIC) {
                adapter.updateList(it)
            }
        }
        viewModel.scoresAll.observe(viewLifecycleOwner) { scoresAll ->
            refreshLayout.isRefreshing = false
            setupScores(scoresAll)
        }
        viewModel.stateOfKindOfApps.observe(viewLifecycleOwner) { state ->
            setupState(state)
        }
        viewModel.lives.observe(viewLifecycleOwner) { countOfLives ->
            setupPulsateAnim(heartFirst, firstHeartState, countOfLives < 1)
            setupPulsateAnim(heartSecond, secondHearState, countOfLives < 2)
            setupPulsateAnim(heartThird, thirdHeartState, countOfLives < 3)
        }
        viewModel.isOnline.observe(viewLifecycleOwner) {
            if (it) {
                heartLayout.visibility = View.GONE
            } else {
                heartLayout.visibility = View.VISIBLE
            }
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

    private fun setupState(state: KindOfApps) = with(binding) {
        if (state == KindOfApps.USEFUL) {
            adapter.updateList(viewModel.usefulApps.value ?: emptyList())
            buttonChangeAdapter.setText(R.string.useful)
        } else {
            adapter.updateList(viewModel.harmfulApps.value ?: emptyList())
            buttonChangeAdapter.setText(R.string.harmful)
        }
    }
}
