package com.ugd3.ugd3_kelompok1.adapters

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ugd3.ugd3_kelompok1.*
import com.ugd3.ugd3_kelompok1.models.Penyelenggara
import java.util.*
import kotlin.collections.ArrayList

class PenyelenggaraAdapter(private var penyelenggaraList: List<Penyelenggara>, context: Context)
    : RecyclerView.Adapter<PenyelenggaraAdapter.ViewHolder>(), Filterable {

    private var filteredPenyelenggaraList: MutableList<Penyelenggara>
    private val context: Context

    init{
        filteredPenyelenggaraList = ArrayList(penyelenggaraList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_penyelenggara_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() : Int{
        return filteredPenyelenggaraList.size
    }

    fun setPenyelenggaraList(penyelenggaraList: Array<Penyelenggara>) {
        this.penyelenggaraList =  penyelenggaraList.toList()
        filteredPenyelenggaraList = penyelenggaraList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val penyelenggara = filteredPenyelenggaraList[position]
        holder.tvNamaPenyelenggara.text = penyelenggara.namaPenyelenggara
        holder.tvJudulDonasi.text = penyelenggara.judulDonasi
        holder.tvKategoriDonasi.text = penyelenggara.kategoriDonasi
        holder.tvTargetJumlahDonasi.text = penyelenggara.targetJumlahDonasi
        holder.tvBatasWaktuDonasi.text = penyelenggara.batasWaktuDonasi

        holder.btnDelete.setOnClickListener(){
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data penyelenggara ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is AddPenyelenggaraActivity) penyelenggara.id?.let { it1 ->
                        context.deletePenyelenggara(it1)
                    }
                }
                .show()
        }
        holder.cvPenyelenggara.setOnClickListener{
            val i = Intent(context, EditPenyelenggaraActivity::class.java)
            i.putExtra("id", penyelenggara.id)
            if(context is AddPenyelenggaraActivity)
                context.startActivityForResult(i, AddPenyelenggaraActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override  fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Penyelenggara> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(penyelenggaraList)
                }else{
                    for(penyelenggara in penyelenggaraList){
                        if(penyelenggara.namaPenyelenggara.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(penyelenggara)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResult: FilterResults){
                filteredPenyelenggaraList.clear()
                filteredPenyelenggaraList.addAll((filterResult.values as List<Penyelenggara>))
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvNamaPenyelenggara: TextView
        var tvJudulDonasi: TextView
        var tvKategoriDonasi: TextView
        var tvTargetJumlahDonasi: TextView
        var tvBatasWaktuDonasi: TextView
        var btnDelete: ImageButton
        var cvPenyelenggara: CardView

        init {
            tvNamaPenyelenggara = itemView.findViewById(R.id.tv_namaPenyelenggara)
            tvJudulDonasi = itemView.findViewById(R.id.tv_judul)
            tvKategoriDonasi = itemView.findViewById(R.id.tv_kategori)
            tvTargetJumlahDonasi = itemView.findViewById(R.id.tv_target)
            tvBatasWaktuDonasi = itemView.findViewById(R.id.tv_batas)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvPenyelenggara = itemView.findViewById(R.id.cv_penyelenggara)
        }
    }
}