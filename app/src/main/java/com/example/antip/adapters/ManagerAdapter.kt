package com.example.antip.adapters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.AppItemSettingsBinding
import com.example.antip.ui.AppManagerFragment
import com.example.antip.model.AppManager
import com.example.antip.viewmodels.DragListener


class ManagerAdapter(
    private var list: ArrayList<AppManager>,
    private val listener: AppManagerFragment,
) : RecyclerView.Adapter<ManagerAdapter.CustomViewHolder?>(), View.OnLongClickListener
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_item_settings, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: ArrayList<AppManager>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<AppManager> = this.list

    val dragInstance: DragListener
        get() = DragListener(listener)


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position],position,this,dragInstance)



    }

    class CustomViewHolder(item: View) :
        RecyclerView.ViewHolder(item) {
        private val binding = AppItemSettingsBinding.bind(item)
        fun bind(app: AppManager, position: Int, managerAdapter: ManagerAdapter, dragInstance:DragListener)
         = with(binding) {
            imageViewSettings.setImageDrawable(app.image)
            nameSettings.text=app.name
            frameLayoutSettings.tag = position
            frameLayoutSettings.setOnLongClickListener(managerAdapter)
            frameLayoutSettings.setOnDragListener(dragInstance)

        }

    }



    override fun onLongClick(v: View?): Boolean {
        val data = ClipData.newPlainText("", "")
        val shadowBuilder = View.DragShadowBuilder(v)
        v?.startDragAndDrop(data, shadowBuilder, v, 0)
        return true
    }
}