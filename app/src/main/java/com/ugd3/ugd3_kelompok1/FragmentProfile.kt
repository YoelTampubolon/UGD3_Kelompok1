package com.ugd3.ugd3_kelompok1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ugd3.ugd3_kelompok1.Donasi.UserDB

class FragmentProfile : Fragment(){
    private lateinit var nama: TextView
    private lateinit var namaProfile: TextView
    private lateinit var email: TextView
    private lateinit var noHp: TextView
    private lateinit var tglLahir: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile ,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnEdit: Button = view.findViewById(R.id.editBtn)
        val imageDelete : ImageView = view.findViewById(R.id.icon_delete)

        val db by lazy { UserDB(activity as HomeActivity) }
        val donateDao = db.donateDao()

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val user = donateDao.getDonate(sharedPreferences.getInt("id", 0))
        nama = view.findViewById(R.id.inputNama)
        namaProfile = view.findViewById(R.id.inputNamaProfile)
        email = view.findViewById(R.id.inputEmail)
        noHp = view.findViewById(R.id.inputPhonenumber)
        tglLahir = view.findViewById(R.id.inputTglLahir)


        // set text
        nama.setText(user.namaLengkap)
        namaProfile.setText(user.namaLengkap)
        email.setText(user.email)
        noHp.setText(user.nomorTelepon)
        tglLahir.setText(user.tanggalLahir)

        imageDelete.setOnClickListener{
            donateDao.deleteDonate(user)
            val moveDeleteImage = Intent(this@FragmentProfile.context, MainActivity::class.java)
            startActivity(moveDeleteImage)
        }




        btnEdit.setOnClickListener{
            val moveEditBtn = Intent(this@FragmentProfile.context, EditProfileActivity::class.java)
            startActivity(moveEditBtn)
        }


        btnLogout.setOnClickListener {
            val moveEditBtn = Intent(this@FragmentProfile.context, MainActivity::class.java)
            startActivity(moveEditBtn)
        }
    }
}
