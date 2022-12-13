package com.ugd3.ugd3_kelompok1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import com.ugd3.ugd3_kelompok1.adapters.PenyelenggaraAdapter
import com.ugd3.ugd3_kelompok1.api.PenyelenggaraApi
import com.ugd3.ugd3_kelompok1.models.Penyelenggara
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddPenyelenggaraActivity : AppCompatActivity() {
    private var penyelenggaraAdapter: PenyelenggaraAdapter? = null
    private var srPenyelenggara: SwipeRefreshLayout? = null
    private var svPenyelenggara: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_penyelenggara)
        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srPenyelenggara = findViewById(R.id.sr_penyelenggara)
        svPenyelenggara = findViewById(R.id.sv_penyelenggara)

        srPenyelenggara?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allPenyelenggara() })
        svPenyelenggara?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                penyelenggaraAdapter!!.filter.filter(s)
                return false
            }
        })


        val fabAdd = findViewById<Button>(R.id.button_create)
        fabAdd.setOnClickListener{
            val i = Intent(this@AddPenyelenggaraActivity, EditPenyelenggaraActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvPenyelenggara = findViewById<RecyclerView>(R.id.rv_penyelenggara)
        penyelenggaraAdapter = PenyelenggaraAdapter(ArrayList(), this)
        rvPenyelenggara.layoutManager = LinearLayoutManager(this)
        rvPenyelenggara.adapter = penyelenggaraAdapter
        allPenyelenggara()
//        setupListener()
//        setupRecyclerView()
    }

    private fun allPenyelenggara(){
        srPenyelenggara!!.isRefreshing = true

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PenyelenggaraApi.GET_ALL_URL, Response.Listener{ response ->
                //   val gson = Gson()
                //   var mahasiswa : Array<Mahasiswa> = gson.fromJson(response, Array<Mahasiswa>::class.java)

                var dataJson = JSONObject(response.toString())
                var penyelenggaraArray = arrayListOf<Penyelenggara>()
                var id : Int = dataJson.getJSONArray("data").length()

                for(i in 0 until id) {
                    var penyelenggara = Penyelenggara(
                        dataJson.getJSONArray("data").getJSONObject(i).getInt("id"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("namaPenyelenggara"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("judulDonasi"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("kategoriDonasi"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("targetJumlahDonasi"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("batasWaktuDonasi"),
                    )
                    penyelenggaraArray.add(penyelenggara)
                }

                var penyelenggara: Array<Penyelenggara> = penyelenggaraArray.toTypedArray()

                penyelenggaraAdapter!!.setPenyelenggaraList(penyelenggara)
                penyelenggaraAdapter!!.filter.filter(svPenyelenggara!!.query)
                srPenyelenggara!!.isRefreshing = false

                if(!penyelenggara.isEmpty())
                //                 Toast.makeText(this@AddDonaturActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    FancyToast.makeText(this, "Data Kosong!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()

            }, Response.ErrorListener{ error ->
                srPenyelenggara!!.isRefreshing = true

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch(e: Exception){
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
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

    fun deletePenyelenggara(id: Int){
        setLoading(true)

        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, PenyelenggaraApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var penyelenggara  = gson.fromJson(response, Penyelenggara:: class.java)


                if(penyelenggara != null)
                    FancyToast.makeText(this, "Data berhasil dihapus", FancyToast.LENGTH_SHORT,  FancyToast.SUCCESS, false).show()
                allPenyelenggara()
            }, Response.ErrorListener{ error ->
                setLoading(false)

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT,  FancyToast.INFO, false
                    ).show()
                }catch(e: Exception){
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT,  FancyToast.ERROR, false).show()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allPenyelenggara()
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
}