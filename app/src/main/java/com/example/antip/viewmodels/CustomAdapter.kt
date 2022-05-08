package com.example.antip.viewmodels

import android.annotation.SuppressLint
import android.content.ClipData
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.App
import com.example.antip.R
import com.example.antip.ui.SettingsFragment


class CustomAdapter(
    private var list: ArrayList<App>,
    private val listener: SettingsFragment,
    private val undefined: App
) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder?>(), View.OnLongClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: ArrayList<App>) {
        this.list = list
    }

    fun getList(): ArrayList<App> = this.list


    fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(v)
                v?.startDragAndDrop(data, shadowBuilder, v, 0)
                return true
            }
        }
        return false
    }

    val dragInstance: DragListener?
        get() = if (listener != null) {
            DragListener(listener, undefined)
        } else {
            Log.e("ERROR", "Listener not initialized")
            null
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.scores?.text = list[position].scores.toString()
        holder.packageName?.text = list[position].name
        holder.iconApp?.setImageDrawable(list[position].image)
        holder.frameLayout?.tag = position
        holder.frameLayout?.setOnLongClickListener(this)
        holder.frameLayout?.setOnDragListener(dragInstance)


    }

    class CustomViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.app_item, parent, false)) {


        var scores: TextView? = null
        var packageName: TextView? = null
        var iconApp: ImageView? = null
        var frameLayout: FrameLayout? = null

        init {
            scores = itemView.findViewById(R.id.scores)
            packageName = itemView.findViewById(R.id.name)
            iconApp = itemView.findViewById(R.id.image_view)
            frameLayout = itemView.findViewById(R.id.frame_layout)

        }
    }

    override fun onLongClick(v: View?): Boolean {
        val data = ClipData.newPlainText("", "")
        val shadowBuilder = View.DragShadowBuilder(v)
        v?.startDragAndDrop(data, shadowBuilder, v, 0)
        return true
    }
}