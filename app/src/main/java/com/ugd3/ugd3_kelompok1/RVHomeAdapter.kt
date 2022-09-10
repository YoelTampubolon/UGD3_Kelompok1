package com.ugd3.ugd3_kelompok1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ugd3.ugd3_kelompok1.Donasi.Home

class RVHomeAdapter(private val data: Array<Home>) : RecyclerView.Adapter<RVHomeAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        //Menghubungkan layout item recycler view kita
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_home,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvJudul.text = currentItem.judulDonasi
        holder.tvGambar.setImageResource(currentItem.gambarDonasi)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvJudul : TextView = itemView.findViewById(R.id.judulDonasi)
        val tvGambar : ImageView = itemView.findViewById(R.id.imageDonasi)
    }
}