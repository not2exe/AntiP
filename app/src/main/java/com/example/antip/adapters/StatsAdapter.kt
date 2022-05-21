package com.example.antip.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.databinding.StatsItemBinding
import com.example.antip.model.db.DailyStatsEntry

class StatsAdapter(private var list: ArrayList<DailyStatsEntry>) :
    RecyclerView.Adapter<StatsAdapter.StatsHolder>() {


    class StatsHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = StatsItemBinding.bind(item)
        fun bind(el: DailyStatsEntry) = with(binding) {
            dateTv.text = el.date
            statsTv.text = el.scores.toString()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stats_item, parent, false)
        return StatsHolder(view)
    }

    override fun onBindViewHolder(holder: StatsHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int = list.size

    fun updateList(list: ArrayList<DailyStatsEntry>) {
        this.list = list
        notifyDataSetChanged()
    }


}
