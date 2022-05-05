package com.example.antip.viewmodels

import android.content.ClipData
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.App
import com.example.antip.R
import org.w3c.dom.Text

interface CustomListener {
    fun setEmptyList(visibility: Int, recyclerView: Int)
}

class CustomAdapter(private var list: List<App>, private val listener: CustomListener?)
    : RecyclerView.Adapter<CustomAdapter.CustomViewHolder?>(), View.OnTouchListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: MutableList<App>) {
        this.list = list
    }

    fun getList(): MutableList<App> = this.list.toMutableList()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val data =ClipData.newPlainText("","")
                val shadowBuilder = View.DragShadowBuilder(v)
                v?.startDragAndDrop(data, shadowBuilder, v, 0)
                return true
            }
        }
        return false
    }

    val dragInstance: DragListener?
        get() = if (listener != null) {
            DragListener(listener)
        } else {
            Log.e("ERROR", "Listener not initialized")
            null
        }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.scores?.text = list[position].scores.toString()
        holder.packageName?.text = list[position].packageName
        holder.iconApp?.setImageDrawable(list[position].image)
        holder.frameLayout?.tag = position
        holder.frameLayout?.setOnTouchListener(this)
        holder.frameLayout?.setOnDragListener(DragListener(listener!!))


    }

    class CustomViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.app_item, parent, false)) {


        var scores:TextView?=null
        var packageName:TextView?=null
        var iconApp:ImageView?=null
        var frameLayout: FrameLayout? = null

        init {
            scores=itemView.findViewById(R.id.scores)
            packageName=itemView.findViewById(R.id.package_name)
            iconApp=itemView.findViewById(R.id.image_view)
            frameLayout = itemView.findViewById(R.id.frame_layout)

        }
    }
}