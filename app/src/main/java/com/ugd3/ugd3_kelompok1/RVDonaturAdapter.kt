package com.ugd3.ugd3_kelompok1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ugd3.ugd3_kelompok1.Donasi.Donatur

class RVDonaturAdapter(private val data: Array<Donatur>) : RecyclerView.Adapter<RVDonaturAdapter.viewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        //Menghubungkan layout item recycler view kita
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_donatur,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvNama.text = currentItem.name
        holder.tvDetails.text = "${currentItem.nominal} - ${currentItem.alamat}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvNama : TextView = itemView.findViewById(R.id.tv_nama)
        val tvDetails : TextView = itemView.findViewById(R.id.tv_details)
    }
}