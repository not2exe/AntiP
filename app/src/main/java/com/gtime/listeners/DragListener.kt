package com.gtime.listeners

import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gtime.adapters.ManagerAdapter
import com.gtime.ui.stateholders.AppManagerFragmentViewModel

class DragListener(private val viewModel: AppManagerFragmentViewModel) :
    View.OnDragListener {
    private var isDropped = false
    
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                if (v.tag == null) return false

                isDropped = true
                val viewSource = event.localState as View?
                val target: RecyclerView = v.parent as RecyclerView
                val positionTarget = v.tag as Int
                if (viewSource == null) return false

                val source = viewSource.parent as RecyclerView
                val positionSource = viewSource.tag as Int

                viewModel.handleChanges(
                    sourceId = source.id,
                    targetId = target.id,
                    positionSource = positionSource,
                    positionTarget = positionTarget,
                    sourceElem = (source.adapter as ManagerAdapter).list[positionSource]
                )
            }
        }
        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true

    }
}
