package com.example.antip.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.model.App
import com.example.antip.R
import com.example.antip.databinding.AppItemBinding

class AppAdapter(private var list: ArrayList<App>) : RecyclerView.Adapter<AppAdapter.AppHolder>() {


    class AppHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = AppItemBinding.bind(item)
        fun bind(app: App) = with(binding) {
            imageView.setImageDrawable(app.image)
            scores.text = app.scores.toString()
            name.text=app.name

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

    fun updateList(list: ArrayList<App>) {
        this.list = list
        notifyDataSetChanged()
    }



}
