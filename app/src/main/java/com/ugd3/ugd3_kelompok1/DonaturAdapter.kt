package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_donatur_adapter.view.*

class DonaturAdapter(private val data: ArrayList<Donaturs>, private val listener: OnAdapterListener) : RecyclerView.Adapter<DonaturAdapter.NoteViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):DonaturAdapter.NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_donatur_adapter,parent,false)
        )
    }

    override fun onBindViewHolder(holder: DonaturAdapter.NoteViewHolder, position: Int) {
        val note = data[position]
        holder.view.tv_nama.text = note.name
        holder.view.tv_nominal.text = note.nominal
        holder.view.tv_alamat.text = note.alamat
        holder.view.tv_nama.setOnClickListener{
            listener.onClick(note)
        }
        holder.view.ic_edit.setOnClickListener{
            listener.onUpdate(note)
        }
        holder.view.icon_delete.setOnClickListener{
            listener.onDelete(note)
        }
    }

    override fun getItemCount() = data.size

    inner class NoteViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Donaturs>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(donaturs: Donaturs)
        fun onUpdate(donaturs: Donaturs)
        fun onDelete(donaturs: Donaturs)
    }
}