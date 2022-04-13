package com.example.antip

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.databinding.AppItemBinding

class AppAdapter : RecyclerView.Adapter<AppAdapter.AppHolder>() {
    val appList = ArrayList<App>()

    class AppHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = AppItemBinding.bind(item)
        fun bind(app: App) = with(binding) {
            imageView.setImageDrawable(app.image)
            scores.text = app.scores.toString()
            packageName.text=app.packageName

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)
        return AppHolder(view)
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        holder.bind(appList[position])

    }

    override fun getItemCount(): Int {
        return appList.size
    }

    fun addApp(app: App) {
        appList.add(app)
        notifyDataSetChanged()
    }
}