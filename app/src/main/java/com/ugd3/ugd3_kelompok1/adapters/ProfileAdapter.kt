package com.ugd3.ugd3_kelompok1.adapters

import android.content.Context
import android.content.Intent
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
import com.ugd3.ugd3_kelompok1.EditProfileActivity
import com.ugd3.ugd3_kelompok1.MainActivity
import com.ugd3.ugd3_kelompok1.R
import com.ugd3.ugd3_kelompok1.models.Profile
import java.util.*
import kotlin.collections.ArrayList

class ProfileAdapter(private var profileList: List<Profile>, context: Context):
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>(), Filterable {

        private var filteredProfileList: MutableList<Profile>
        private val context: Context

        init {
            filteredProfileList = ArrayList(profileList)
            this.context = context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_profile, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return filteredProfileList.size
        }

        fun setProfileList(profileList: Array<Profile>){
            this.profileList = profileList.toList()
            filteredProfileList = profileList.toMutableList()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val profile = filteredProfileList[position]
            holder.tvNamaLengkap.text = profile.namaLengkap
            holder.tvEmail.text = profile.email
            holder.tvPassword.text = profile.password
            holder.tvTanggalLahir.text = profile.tanggalLahir
            holder.tvNomorTelepon.text = profile.nomorTelepon

        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    val charSequenceString = charSequence.toString()
                    val filtered: MutableList<Profile> = java.util.ArrayList()
                    if(charSequenceString.isEmpty()){
                        filtered.addAll(profileList)
                    }else{
                        for (profile in profileList){
                            if(profile.namaLengkap.lowercase(Locale.getDefault())
                                    .contains(charSequenceString.lowercase(Locale.getDefault()))

                            )filtered.add(profile)

                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filtered
                    return filterResults

                }

                override fun publishResults( CharSequence: CharSequence, filterResults: FilterResults) {
                    filteredProfileList.clear()
                    filteredProfileList.addAll(filterResults.values as List<Profile>)
                    notifyDataSetChanged()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var tvNamaLengkap: TextView
            var tvEmail: TextView
            var tvPassword: TextView
            var tvTanggalLahir: TextView
            var tvNomorTelepon: TextView
            var cvProfile: CardView

            init {
                tvNamaLengkap = itemView.findViewById(R.id.namaInput)
                tvEmail = itemView.findViewById(R.id.emailInput)
                tvPassword = itemView.findViewById(R.id.passwordInput)
                tvTanggalLahir = itemView.findViewById(R.id.inputTextTanggalLahir)
                tvNomorTelepon = itemView.findViewById(R.id.nomorTeleponInput)
                cvProfile = itemView.findViewById(R.id.cv_profile)
            }

        }
}