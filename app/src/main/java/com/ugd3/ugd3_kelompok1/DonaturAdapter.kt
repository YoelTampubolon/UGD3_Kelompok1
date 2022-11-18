package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_donatur_adapter.view.*
import android.content.Context
import android.content.Intent
import android.widget.*
import androidx.cardview.widget.CardView
import com.ugd3.ugd3_kelompok1.R
import com.ugd3.ugd3_kelompok1.models.Donatur
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ugd3.ugd3_kelompok1.AddDonaturActivity.Companion.LAUNCH_ADD_ACTIVITY
import java.util.*
import kotlin.collections.ArrayList

class DonaturAdapter(private var data: ArrayList<Donatur>, context: Context) : RecyclerView.Adapter<DonaturAdapter.NoteViewHolder>(){

    private var filteredDonasiList: MutableList<Donatur>
    private val context: Context

    init{
        filteredDonasiList = ArrayList(data)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): NoteViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_donatur_adapter, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int){
        val donatur = filteredDonasiList[position]
        holder.view.tv_nama.text = donatur.name
        holder.view.tv_nominal.text = donatur.nominal
        holder.view.tv_alamat.text = donatur.alamat

        holder.view.btn_delete.setOnClickListener(){
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is MainActivity) donatur.id?.let { it1 ->
                        context.deleteMahasiswa(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvDonatur.setOnClickListener{
            val i = Intent(context, AddDonaturActivity::class.java)
            i.putExtra("id", donatur.id)
            if(context is AddDonaturActivity)
                context.startActivityForResult(i, AddDonaturActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getItemCount() : Int{
        return filteredDonasiList.size
    }

    inner class NoteViewHolder(val view: View) :
        RecyclerView.ViewHolder(view){
        var tvNama: TextView
        var tvNominal: TextView
        var tvAlamat: TextView
        var cvDonatur: CardView

        init {
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvNominal = itemView.findViewById(R.id.tv_nominal)
            tvAlamat = itemView.findViewById(R.id.tv_alamat)
            cvDonatur = itemView.findViewById(R.id.cv_donatur)
        }

        }



//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(list: List<Donatur>) {
//        data.clear()
//        data.addAll(list)
//        notifyDataSetChanged()
//    }
//
//    interface OnAdapterListener{
//        fun onClick(donatur: Donatur)
//        fun onUpdate(donatur: Donatur)
//        fun onDelete(donatur: Donatur)
//    }
}