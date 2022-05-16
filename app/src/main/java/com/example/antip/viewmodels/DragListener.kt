package com.example.antip.viewmodels

import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.adapters.CustomAdapter
import com.example.antip.ui.SettingsFragment

class DragListener internal constructor(
    private val listener: SettingsFragment,
    private val undefined: AppS
) :
    View.OnDragListener {
    private var isDropped = false
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
                        val adapterSource = source.adapter as CustomAdapter?
                        val sourceList = adapterSource?.getList()

                        if (adapterSource?.getList()?.get(0)?.name == undefined.name)
                            return false

                        if (adapterSource?.getList()?.size == 1) {
                            sourceList?.add(undefined)
                        }

                        sourceList?.let { adapterSource.updateList(it) }


                        val positionSource = viewSource.tag as Int
                        val list: AppS? = adapterSource?.getList()?.get(positionSource)


                        val listSource = adapterSource?.getList()?.apply {
                            removeAt(positionSource)
                        }
                        listSource?.let { adapterSource.updateList(it) }


                        val adapterTarget = target.adapter as CustomAdapter?
                        val customListTarget = adapterTarget?.getList()
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

                        val usefulEdit=listener.context?.getSharedPreferences("nameOfUseful", 0)?.edit()
                        val harmfulEdit=listener.context?.getSharedPreferences("nameOfHarmful", 0)?.edit()

                        when((viewSource.parent as RecyclerView).id) {
                            R.id.rvUseful -> {
                                usefulEdit?.remove(list?.name)
                                usefulEdit?.apply()
                                if (target.id == R.id.rvHarmful) {
                                    harmfulEdit?.putString(list?.name, list?.name)
                                    harmfulEdit?.apply()
                                }
                            }
                            R.id.rvHarmful -> {
                                harmfulEdit?.remove(list?.name)
                                harmfulEdit?.apply()
                                if (target.id == R.id.rvUseful) {
                                    usefulEdit?.putString(list?.name, list?.name)
                                    usefulEdit?.apply()

                                }

                            }
                            R.id.rvOthers -> {
                                when (target.id) {
                                    R.id.rvHarmful -> {
                                        harmfulEdit?.putString(list?.name, list?.name)
                                        harmfulEdit?.apply()

                                    }
                                    R.id.rvUseful -> {
                                        usefulEdit?.putString(list?.name, list?.name)
                                        usefulEdit?.apply()

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
