package com.ugd3.ugd3_kelompok1

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.ugd3.ugd3_kelompok1.Donasi.Constant
import com.ugd3.ugd3_kelompok1.Donasi.DonaturDB
import com.ugd3.ugd3_kelompok1.api.DonaturApi
import com.ugd3.ugd3_kelompok1.databinding.ActivityAddDonaturBinding
import com.ugd3.ugd3_kelompok1.models.Donatur
import kotlinx.android.synthetic.main.activity_add_donatur.*
import kotlinx.android.synthetic.main.activity_edit_donatur.*
import kotlinx.android.synthetic.main.fragment_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddDonaturActivity : AppCompatActivity() {

//    val db by lazy {DonaturDB(this) }
    private var donaturAdapter: DonaturAdapter? = null
    private var srDonatur: SwipeRefreshLayout? = null
    private var svDonatur: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donatur)
        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srDonatur = findViewById(R.id.sr_donatur)
        svDonatur = findViewById(R.id.sv_donatur)

        srDonatur?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allDonatur() })
        svDonatur?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                donaturAdapter!!.filter.filter(s)
                return false
            }
        })


        val fabAdd = findViewById<Button>(R.id.button_create)
        fabAdd.setOnClickListener{
            val i = Intent(this@AddDonaturActivity, EditDonaturActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvDonatur = findViewById<RecyclerView>(R.id.rv_donaturs)
        donaturAdapter = DonaturAdapter(ArrayList(), this)
        rvDonatur.layoutManager = LinearLayoutManager(this)
        rvDonatur.adapter = donaturAdapter
        allDonatur()
//        setupListener()
//        setupRecyclerView()
    }

     private fun allDonatur(){
        srDonatur!!.isRefreshing = true

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, DonaturApi.GET_ALL_URL, Response.Listener{ response ->
                //   val gson = Gson()
                //   var mahasiswa : Array<Mahasiswa> = gson.fromJson(response, Array<Mahasiswa>::class.java)

                var dataJson = JSONObject(response.toString())
                var donaturArray = arrayListOf<Donatur>()
                var id : Int = dataJson.getJSONArray("data").length()

                for(i in 0 until id) {
                    var donatur = Donatur(
                        dataJson.getJSONArray("data").getJSONObject(i).getInt("id"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("name"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("nominal"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("alamat"),
                    )
                    donaturArray.add(donatur)
                }

                var donatur: Array<Donatur> = donaturArray.toTypedArray()

                donaturAdapter!!.setDonaturList(donatur)
                donaturAdapter!!.filter.filter(svDonatur!!.query)
                srDonatur!!.isRefreshing = false

                if(!donatur.isEmpty())
 //                 Toast.makeText(this@AddDonaturActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@AddDonaturActivity, "Data Kosong!", Toast.LENGTH_SHORT)

            }, Response.ErrorListener{ error ->
                srDonatur!!.isRefreshing = true

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddDonaturActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch(e: Exception){
                    Toast.makeText(this@AddDonaturActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deleteDonatur(id: Int){
        setLoading(true)

        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, DonaturApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var donatur  = gson.fromJson(response, Donatur:: class.java)


                if(donatur != null)
                    Toast.makeText(this@AddDonaturActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                allDonatur()
            }, Response.ErrorListener{ error ->
                setLoading(false)

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddDonaturActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch(e: Exception){
                    Toast.makeText(this@AddDonaturActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }




    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allDonatur()
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.GONE
        }
    }



//    private fun setupRecyclerView() {
//        donaturAdapter = DonaturAdapter(arrayListOf(), object :
//            DonaturAdapter.OnAdapterListener {
//            override fun onClick(donaturs: Donaturs) {
//                intentEdit(donaturs.id, Constant.TYPE_READ)
//            }
//            override fun onUpdate(donaturs: Donaturs) {
//                intentEdit(donaturs.id, Constant.TYPE_UPDATE)
//            }
//            override fun onDelete(donaturs: Donaturs) {
//                deleteDialog(donaturs)
//            }
//        })
//        rv_donaturs.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            adapter = donaturAdapter
//        }
//    }

//    private fun deleteDialog(donaturs: Donaturs){
//        val alertDialog = AlertDialog.Builder(this)
//        alertDialog.apply {
//            setTitle("Confirmation")
//            setMessage("Are You Sure to delete this data From ${donaturs.name}?")
//            setNegativeButton("Cancel", DialogInterface.OnClickListener
//            { dialogInterface, i ->
//                dialogInterface.dismiss()
//            })
//            setPositiveButton("Delete", DialogInterface.OnClickListener
//            { dialogInterface, i ->
//                dialogInterface.dismiss()
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.donaturDao().deleteDonaturs(donaturs)
//                    loadData()
//                }
//            })
//        }
//        alertDialog.show()
//    }
//    override fun onStart() {
//        super.onStart()
//        loadData()
//    }
//
//    fun loadData() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = db.donaturDao().getDonaturs()
//            Log.d("AddDonaturActivity","dbResponse: $notes")
//            withContext(Dispatchers.Main){
//                donaturAdapter.setData( notes )
//            }
//        }
//    }
//    fun setupListener() {
//        button_create.setOnClickListener{
//            intentEdit(0,Constant.TYPE_CREATE)
//        }
//    }
//
//    fun intentEdit(noteId : Int, intentType: Int) {
//        startActivity(
//            Intent(applicationContext, EditDonaturActivity::class.java)
//                .putExtra("intent_id", noteId)
//                .putExtra("intent_type", intentType)
//        )
//    }



}