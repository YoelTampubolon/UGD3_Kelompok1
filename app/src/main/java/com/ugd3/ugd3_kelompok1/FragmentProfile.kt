package com.ugd3.ugd3_kelompok1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FragmentProfile : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        //Proses menghubungkan layout fragment_mahasiswa.xml dengan fragment ini
        return inflater.inflate(R.layout.fragment_profile ,container, false)
    }

}