package com.example.prog7314ices.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7314ices.R

class ForecastAdapter(private val items: List<Forecast>)
    : RecyclerView.Adapter<ForecastAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvDate)
        val tvMin: TextView = v.findViewById(R.id.tvMin)
        val tvMax: TextView = v.findViewById(R.id.tvMax)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val f = items[position]
        holder.tvDate.text = f.date
        holder.tvMin.text = "Min: ${f.min}°C"
        holder.tvMax.text = "Max: ${f.max}°C"
    }

    override fun getItemCount(): Int = items.size
}
