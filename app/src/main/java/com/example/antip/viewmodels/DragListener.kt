package com.example.antip.viewmodels

import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.App

class DragListener internal constructor(private val listener: CustomListener) :
    View.OnDragListener {
    private var isDropped = false
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {

                isDropped = true
                var positionTarget = -1
                val viewSource = event.localState as View?



                val target: RecyclerView
                target = v.parent as RecyclerView
                positionTarget = v.tag as Int
                if (viewSource != null && viewSource.parent!=target) {
                    val source = viewSource.parent as RecyclerView
                    val adapterSource = source.adapter as CustomAdapter?
                    val positionSource = viewSource.tag as Int
                    val list: App? = adapterSource?.getList()?.get(positionSource)

                    val listSource = adapterSource?.getList()?.apply {
                        removeAt(positionSource)
                    }
                    listSource?.let { adapterSource.updateList(it) }
                    adapterSource?.notifyDataSetChanged()
                    val adapterTarget = target.adapter as CustomAdapter?
                    val customListTarget = adapterTarget?.getList()
                    if (positionTarget >= 0) {
                        list?.let { customListTarget?.add(positionTarget, it) }
                    } else {
                        list?.let { customListTarget?.add(it) }
                        list?.let{customListTarget?.sortByDescending { it.scores }}
                    }
                    customListTarget?.let { adapterTarget.updateList(it) }
                    adapterTarget?.notifyDataSetChanged()


                }
            }
        }
        if (!isDropped && event.localState != null)
        {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true

    }

}
