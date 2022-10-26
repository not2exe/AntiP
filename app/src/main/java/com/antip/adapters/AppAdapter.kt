package com.antip.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.AppItemBinding
import com.example.antip.model.dataclasses.AppEntity

class AppAdapter(private var list: ArrayList<AppEntity>) : RecyclerView.Adapter<AppAdapter.AppHolder>() {


    class AppHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = AppItemBinding.bind(item)
        fun bind(appEntity: AppEntity) = with(binding) {
            imageView.setImageDrawable(appEntity.image)
            scores.text = appEntity.scores.toString()
            name.text = appEntity.name

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)
        return AppHolder(view)
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: ArrayList<AppEntity>) {
        this.list = list
        notifyDataSetChanged()
    }


}
