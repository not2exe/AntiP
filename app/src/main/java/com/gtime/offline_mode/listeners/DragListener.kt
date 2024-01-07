package com.gtime.offline_mode.listeners

import android.content.Context
import android.view.DragEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gtime.general.model.dataclasses.AppEntity
import com.gtime.offline_mode.ui.stateholders.AppManagerFragmentViewModel
import com.notexe.gtime.R
import com.notexe.gtime.databinding.FragmentAppManagerBinding
import com.notexe.gtime.databinding.ManagerItemBinding

class DragListener(
    private val viewModel: AppManagerFragmentViewModel,
    private val bindingFragment: FragmentAppManagerBinding
) :
    View.OnDragListener {
    private var isDropped = false

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val viewSource = event.localState as View? ?: return false
        val binding = ManagerItemBinding.bind(viewSource)
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                bindingFragment.apply {
                    val imm =
                        root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(autoCompleteTV.windowToken, 0)
                    autoCompleteTV.clearFocus()
                    autoCompleteTV.isFocusableInTouchMode = false
                }
                binding.cardManagerLayout.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.card_selected_background
                )
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                bindingFragment.autoCompleteTV.isFocusableInTouchMode = true
                binding.cardManagerLayout.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.card_unselected_background
                )
            }
            DragEvent.ACTION_DROP -> {
                isDropped = true
                val target = if (v.tag != null) v.parent as RecyclerView else v as RecyclerView
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
