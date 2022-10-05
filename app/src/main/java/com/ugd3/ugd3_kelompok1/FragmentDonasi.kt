package com.ugd3.ugd3_kelompok1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ugd3.ugd3_kelompok1.databinding.FragmentDonasiBinding


class FragmentDonasi : Fragment() {
    private lateinit var binding: FragmentDonasiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donasi,container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnDonatur: Button = view.findViewById(R.id.button1)

        btnDonatur.setOnClickListener(View.OnClickListener {
            val moveDonatur = Intent(this@FragmentDonasi.context, AddDonaturActivity::class.java)
            startActivity(moveDonatur)
        })
    }
}