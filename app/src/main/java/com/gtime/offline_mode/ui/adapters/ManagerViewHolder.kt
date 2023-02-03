package com.gtime.offline_mode.ui.adapters

import android.content.ClipData
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.ManagerItemBinding
import com.gtime.general.KindOfApps
import com.gtime.general.model.dataclasses.AppEntity
import com.gtime.general.model.db.AppDataBaseEntity
import com.gtime.offline_mode.listeners.DragListener
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

class ManagerViewHolder(item: View, private val viewModel: AppManagerFragmentViewModel) :
    RecyclerView.ViewHolder(item) {
    private val binding = ManagerItemBinding.bind(item)

    fun bind(
        appEntity: AppEntity,
        dragInstance: DragListener
    ) = with(binding) {
        setupSpinner(appEntity)
        scores.text = appEntity.scores.toString()
        percents.text = "${appEntity.percentsOsGeneral}%"
        appIcon.setImageDrawable(appEntity.image)
        appName.text = appEntity.name
        cardManagerLayout.setOnLongClickListener {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(it)
            it?.startDragAndDrop(data, shadowBuilder, it, 0)
            true
        }
        cardManagerLayout.tag = appEntity
        cardManagerLayout.setOnDragListener(dragInstance)
    }

    private fun setupSpinner(
        appEntity: AppEntity,
    ) =
        with(binding) {
            val adapter = ArrayAdapter.createFromResource(
                root.context,
                R.array.multiplier_values,
                R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(R.layout.spiner_item)
            multipliersSpinner.adapter = adapter
            val pos = abs(appEntity.multiplier * 10).toInt() % 10
            multipliersSpinner.setSelection(pos)
            multipliersSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val absMultiplier = 1.0 + position / 10.0
                        viewModel.setMultiplier(
                            AppDataBaseEntity(
                                packageName = appEntity.packageName ?: "",
                                kindOfApp = appEntity.kindOfApps,
                                multiplier = if (appEntity.kindOfApps == KindOfApps.TOXIC) (absMultiplier * -1) else absMultiplier,
                                isGame = appEntity.isGame,
                            )
                        )
                    }
                }
        }
}