package com.gtime.listeners

import android.content.Context
import android.view.DragEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.gtime.Constants
import com.gtime.FragmentScope
import com.gtime.adapters.ManagerAdapter
import com.gtime.model.Cache
import com.gtime.model.dataclasses.AppEntity
import javax.inject.Inject

@FragmentScope
class DragListener @Inject constructor(private val cache: Cache) :
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
                val adapterSource = (source.adapter as ManagerAdapter?) ?: return false
                val sourceList = adapterSource.list.toMutableList()
                val adapterTarget = (target.adapter as ManagerAdapter?) ?: return false
                val customListTarget = adapterTarget.list.toMutableList()
                val positionSource = viewSource.tag as Int

                val sourceElem: AppEntity = sourceList[positionSource]
                if(sourceElem.name==Constants.EMPTY)return false
                sourceList.removeAt(positionSource)
                adapterSource.updateList(sourceList)

                if (positionTarget >= 0) {
                    customListTarget.add(positionTarget, sourceElem)

                } else {
                    customListTarget.add(sourceElem)
                }
                adapterTarget.updateList(customListTarget)

                if (target != source) {
                    handleCacheChanges(source, target, sourceElem.name ?: "")
                }
            }
        }

        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true

    }

    private fun handleCacheChanges(source: RecyclerView, target: RecyclerView, name: String) {
        when (source.id) {
            R.id.rvUseful -> {
                cache.removeFromUseful(name)
                if (target.id == R.id.rvHarmful) {
                    cache.inputIntoHarmful(name)
                }
            }
            R.id.rvHarmful -> {
                cache.removeFromHarmful(name)
                if (target.id == R.id.rvUseful) {
                    cache.inputIntoUseful(name)

                }
            }
            R.id.rvOthers -> {
                when (target.id) {
                    R.id.rvHarmful -> {
                        cache.inputIntoHarmful(name)

                    }
                    R.id.rvUseful -> {
                        cache.inputIntoUseful(name)
                    }
                }
            }
        }
    }

}
