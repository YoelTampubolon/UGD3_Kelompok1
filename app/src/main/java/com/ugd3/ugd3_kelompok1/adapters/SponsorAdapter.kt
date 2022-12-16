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
import com.ugd3.ugd3_kelompok1.models.Sponsor
import java.util.*
import kotlin.collections.ArrayList

class SponsorAdapter(private var sponsorList: List<Sponsor>, context: Context)
    : RecyclerView.Adapter<SponsorAdapter.ViewHolder>(), Filterable {

    private var filteredSponsorList: MutableList<Sponsor>
    private val context: Context

    init{
        filteredSponsorList = ArrayList(sponsorList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_sponsor_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() : Int{
        return filteredSponsorList.size
    }

    fun setSponsorList(SponsorList: Array<Sponsor>) {
        this.sponsorList =  SponsorList.toList()
        filteredSponsorList = SponsorList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val Sponsor = filteredSponsorList[position]
        holder.tvNamaSponsor.text = Sponsor.namaSponsor
        holder.tvUsia.text = Sponsor.usia
        holder.tvNominal.text = Sponsor.nominal
        holder.tvTujuanDonasi.text = Sponsor.tujuanDonasi

        holder.btnDelete.setOnClickListener(){
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Sponsor ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is AddSponsorActivity) Sponsor.id?.let { it1 ->
                        context.deleteSponsor(it1)
                    }
                }
                .show()
        }
        holder.cvSponsor.setOnClickListener{
            val i = Intent(context, EditSponsorActivity::class.java)
            i.putExtra("id", Sponsor.id)
            if(context is AddSponsorActivity)
                context.startActivityForResult(i, AddSponsorActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override  fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Sponsor> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(sponsorList)
                }else{
                    for(Sponsor in sponsorList){
                        if(Sponsor.namaSponsor.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(Sponsor)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResult: FilterResults){
                filteredSponsorList.clear()
                filteredSponsorList.addAll((filterResult.values as List<Sponsor>))
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvNamaSponsor: TextView
        var tvUsia: TextView
        var tvNominal: TextView
        var tvTujuanDonasi: TextView
        var btnDelete: ImageButton
        var cvSponsor: CardView

        init {
            tvNamaSponsor = itemView.findViewById(R.id.tv_namaSponsor)
            tvUsia = itemView.findViewById(R.id.tv_usia)
            tvNominal = itemView.findViewById(R.id.tv_nominal)
            tvTujuanDonasi = itemView.findViewById(R.id.tv_tujuanDonasi)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvSponsor = itemView.findViewById(R.id.cv_sponsor)
        }
    }
}