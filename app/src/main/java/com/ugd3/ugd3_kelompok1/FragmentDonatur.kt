package com.ugd3.ugd3_kelompok1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ugd3.ugd3_kelompok1.Donasi.Donatur


class FragmentDonatur : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        //Proses menghubungkan layout fragment_mahasiswa.xml dengan fragment ini
        return inflater.inflate(R.layout.fragment_donatur ,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter: RVDonaturAdapter = RVDonaturAdapter(Donatur.listOfDonatur)

        //Menghubungkan rvMahasiswa dengan recycler view yang ada pada layout
        val rvDonatur : RecyclerView = view.findViewById(R.id.rv_donatur)

        //Set layout manager dari recycler view
        rvDonatur.layoutManager = layoutManager

        //tidak mengubah size recycler view jika terdapat item ditambahkan atau dikurangkan
        rvDonatur.setHasFixedSize(true)

        //Set Adapter dari recycler view.
        rvDonatur.adapter = adapter
    }
}