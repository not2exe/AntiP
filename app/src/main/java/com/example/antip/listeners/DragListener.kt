package com.example.antip.listeners

import android.annotation.SuppressLint
import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.adapters.ManagerAdapter
import com.example.antip.model.Cash
import com.example.antip.model.dataclasses.AppManager
import com.example.antip.ui.AppManagerFragment

class DragListener internal constructor(
    private val listener: AppManagerFragment
) :
    View.OnDragListener {
    private val undefined: AppManager =
        AppManager(listener.requireContext().getDrawable(R.drawable.undefined)!!, "Empty")
    private var isDropped = false

    @SuppressLint("ResourceType")
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                if (v.tag != null) {
                    isDropped = true
                    var positionTarget = -1
                    val viewSource = event.localState as View?
                    val target: RecyclerView = v.parent as RecyclerView
                    positionTarget = v.tag as Int
                    if (viewSource != null) {
                        val source = viewSource.parent as RecyclerView
                        val adapterSource = source.adapter as ManagerAdapter?
                        val sourceList = adapterSource?.getList()
                        val adapterTarget = target.adapter as ManagerAdapter?
                        val customListTarget = adapterTarget?.getList()

                        if (adapterSource?.getList()?.get(0)?.name == undefined.name)
                            return false

                        if (adapterSource?.getList()?.size == 1) {
                            sourceList?.add(undefined)
                        }

                        sourceList?.let { adapterSource.updateList(it) }


                        val positionSource = viewSource.tag as Int
                        val list: AppManager? = adapterSource?.getList()?.get(positionSource)


                        val listSource = adapterSource?.getList()?.apply {
                            removeAt(positionSource)
                        }
                        listSource?.let { adapterSource.updateList(it) }



                        if (positionTarget >= 0) {
                            list?.let { customListTarget?.add(positionTarget, it) }

                        } else {
                            list?.let { customListTarget?.add(it) }
                        }

                        if (customListTarget != null) {
                            for (i in 0 until customListTarget.size) {
                                if (customListTarget[i].name == undefined.name)
                                    customListTarget.removeAt(i)


                            }
                        }

                        customListTarget?.let { adapterTarget.updateList(it) }
                        adapterTarget?.notifyDataSetChanged()
                        if (v.parent != viewSource.parent) {
                            val cash: Cash = Cash(listener.requireContext())

                            when ((viewSource.parent as RecyclerView).id) {
                                R.id.rvUseful -> {
                                    list?.name?.let { cash.removeFromUseful(it) }
                                    if (target.id == R.id.rvHarmful) {
                                        list?.name?.let { cash.inputIntoHarmful(it) }
                                    }
                                }
                                R.id.rvHarmful -> {
                                    list?.name?.let { cash.removeFromHarmful(it) }
                                    if (target.id == R.id.rvUseful) {
                                        list?.name?.let { cash.inputIntoUseful(it) }

                                    }

                                }
                                R.id.rvOthers -> {
                                    when (target.id) {
                                        R.id.rvHarmful -> {
                                            list?.name?.let { cash.inputIntoHarmful(it) }

                                        }
                                        R.id.rvUseful -> {
                                            list?.name?.let { cash.inputIntoUseful(it) }
                                        }
                                    }

                                }
                            }
                        }


                    }
                }
            }
        }
        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true

    }

}
