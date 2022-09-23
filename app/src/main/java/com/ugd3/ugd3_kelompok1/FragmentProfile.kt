package com.ugd3.ugd3_kelompok1

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.app.Activity
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class FragmentProfile : Fragment(){

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
        val editProfile: TextView = view.findViewById(R.id.editProfil)

        editProfile.setOnClickListener{
            activity?.let { it1 ->

            }
        }

        btnLogout.setOnClickListener {
           activity?.let { it1 ->
               MaterialAlertDialogBuilder(it1)
                   .setTitle("Apakah anda ingin keluar?")
                   .setNegativeButton("No") {dialog, which ->

                   }
                   .setPositiveButton("yes") {dialog, which ->
                       activity?.finish()
                   }
                   .show()
           }
        }
    }



}
