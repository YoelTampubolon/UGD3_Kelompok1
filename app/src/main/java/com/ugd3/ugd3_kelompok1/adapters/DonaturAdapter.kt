package com.ugd3.ugd3_kelompok1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.widget.*
import androidx.cardview.widget.CardView
import com.ugd3.ugd3_kelompok1.R
import com.ugd3.ugd3_kelompok1.models.Donatur
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ugd3.ugd3_kelompok1.AddDonaturActivity
import com.ugd3.ugd3_kelompok1.EditDonaturActivity
import java.util.*
import kotlin.collections.ArrayList

class DonaturAdapter(private var donaturList: List<Donatur>, context: Context)
    : RecyclerView.Adapter<DonaturAdapter.ViewHolder>(),Filterable{

    private var filteredDonaturList: MutableList<Donatur>
    private val context: Context

    init{
        filteredDonaturList = ArrayList(donaturList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_donatur_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() : Int{
        return filteredDonaturList.size
    }

    fun setDonaturList(donaturList: Array<Donatur>) {
        this.donaturList =  donaturList.toList()
        filteredDonaturList = donaturList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val donatur = filteredDonaturList[position]
        holder.tvNama.text = donatur.name
        holder.tvNominal.text = donatur.nominal
        holder.tvAlamat.text = donatur.alamat

        holder.btnDelete.setOnClickListener(){
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data donatur ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is AddDonaturActivity) donatur.id?.let { it1 ->
                        context.deleteDonatur(it1)
                    }
                }
                .show()
        }
        holder.cvDonatur.setOnClickListener{
            val i = Intent(context, EditDonaturActivity::class.java)
            i.putExtra("id", donatur.id)
            if(context is AddDonaturActivity)
                context.startActivityForResult(i, AddDonaturActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override  fun performFiltering(charSequence: CharSequence): FilterResults{
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Donatur> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(donaturList)
                }else{
                    for(donatur in donaturList){
                        if(donatur.name.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(donatur)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResult: FilterResults){
                filteredDonaturList.clear()
                filteredDonaturList.addAll((filterResult.values as List<Donatur>))
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvNama: TextView
        var tvNominal: TextView
        var tvAlamat: TextView
        var btnDelete: ImageButton
        var cvDonatur: CardView

            init {
                tvNama = itemView.findViewById(R.id.tv_nama)
                tvNominal = itemView.findViewById(R.id.tv_nominal)
                tvAlamat = itemView.findViewById(R.id.tv_alamat)
                btnDelete = itemView.findViewById(R.id.btn_delete)
                cvDonatur = itemView.findViewById(R.id.cv_donatur)
            }
        }
}