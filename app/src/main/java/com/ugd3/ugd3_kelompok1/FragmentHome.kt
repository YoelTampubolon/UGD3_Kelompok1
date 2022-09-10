package com.ugd3.ugd3_kelompok1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ugd3.ugd3_kelompok1.Donasi.Home


class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter: RVHomeAdapter = RVHomeAdapter(Home.listOfHome)

        //Menghubungkan rvMahasiswa dengan recycler view yang ada pada layout
        val rvHome : RecyclerView = view.findViewById(R.id.rv_home)

        //Set layout manager dari recycler view
        rvHome.layoutManager = layoutManager

        //tidak mengubah size recycler view jika terdapat item ditambahkan atau dikurangkan
        rvHome.setHasFixedSize(true)

        //Set Adapter dari recycler view.
        rvHome.adapter = adapter
    }
}
