package com.ugd3.ugd3_kelompok1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.ugd3.ugd3_kelompok1.Donasi.Home
import com.ugd3.ugd3_kelompok1.Donasi.UserDB


class FragmentHome : Fragment() {
    private lateinit var namaView: TextView
    private var queue: RequestQueue? = null

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
        namaView = view.findViewById(R.id.namaView)
        queue = Volley.newRequestQueue(requireActivity())

        val rvHome : RecyclerView = view.findViewById(R.id.rv_home)



//        val db by lazy { UserDB(activity as HomeActivity) }
//        val donateDao = db.donateDao()

        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)

//        val user = donateDao.getDonate(sharedPreferences.getInt("id", 0))

        

        namaView.text = sharedPreferences!!.getString("nama", "")

        //Set layout manager dari recycler view
        rvHome.layoutManager = layoutManager

        //tidak mengubah size recycler view jika terdapat item ditambahkan atau dikurangkan
        rvHome.setHasFixedSize(true)

        //Set Adapter dari recycler view.
        rvHome.adapter = adapter
    }
}
