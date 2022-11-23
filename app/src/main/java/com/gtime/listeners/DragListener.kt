package com.gtime.listeners

import android.view.DragEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.ManagerItemBinding
import com.gtime.model.dataclasses.AppEntity
import com.gtime.ui.stateholders.AppManagerFragmentViewModel

class DragListener(private val viewModel: AppManagerFragmentViewModel) :
    View.OnDragListener {
    private var isDropped = false

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val viewSource = event.localState as View? ?: return false
        val binding = ManagerItemBinding.bind(viewSource)
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED->{
                binding.cardManagerLayout.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.card_selected_background
                )
            }
            DragEvent.ACTION_DRAG_ENDED->{
                binding.cardManagerLayout.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.card_unselected_background
                )
            }
            DragEvent.ACTION_DROP -> {
                isDropped = true
                val target = if(v.tag!=null) v.parent as RecyclerView else v as RecyclerView
                val source = viewSource.parent as RecyclerView
                val elem = viewSource.tag as AppEntity
                viewModel.handleChanges(
                    sourceId = source.id,
                    targetId = target.id,
                    sourceElem = elem
                )
            }
        }
        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true

    }
}
