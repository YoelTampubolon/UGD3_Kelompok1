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
import com.ugd3.ugd3_kelompok1.adapters.SponsorAdapter
import com.ugd3.ugd3_kelompok1.api.SponsorApi
import com.ugd3.ugd3_kelompok1.models.Sponsor
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddSponsorActivity : AppCompatActivity() {

    private var sponsorAdapter: SponsorAdapter? = null
    private var srSponsor: SwipeRefreshLayout? = null
    private var svSponsor: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sponsor)
        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srSponsor = findViewById(R.id.sr_sponsor)
        svSponsor = findViewById(R.id.sv_sponsor)

        srSponsor?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allSponsor() })
        svSponsor?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                sponsorAdapter!!.filter.filter(s)
                return false
            }
        })


        val fabAdd = findViewById<Button>(R.id.button_create)
        fabAdd.setOnClickListener{
            val i = Intent(this@AddSponsorActivity, EditSponsorActivity::class.java)
            startActivityForResult(i, AddSponsorActivity.LAUNCH_ADD_ACTIVITY)
        }

        val rvSponsor = findViewById<RecyclerView>(R.id.rv_sponsor)
        sponsorAdapter = SponsorAdapter(ArrayList(), this)
        rvSponsor.layoutManager = LinearLayoutManager(this)
        rvSponsor.adapter = sponsorAdapter
        allSponsor()
    }

    private fun allSponsor(){
        srSponsor!!.isRefreshing = true

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, SponsorApi.GET_ALL_URL, Response.Listener{ response ->

                var dataJson = JSONObject(response.toString())
                var sponsorArray = arrayListOf<Sponsor>()
                var id : Int = dataJson.getJSONArray("data").length()

                for(i in 0 until id) {
                    var Sponsor = Sponsor(
                        dataJson.getJSONArray("data").getJSONObject(i).getInt("id"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("namaSponsor"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("usia"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("nominal"),
                        dataJson.getJSONArray("data").getJSONObject(i).getString("tujuanDonasi"),
                    )
                    sponsorArray.add(Sponsor)
                }

                var sponsor: Array<Sponsor> = sponsorArray.toTypedArray()

                sponsorAdapter!!.setSponsorList(sponsor)
                sponsorAdapter!!.filter.filter(svSponsor!!.query)
                srSponsor!!.isRefreshing = false

                if(!sponsor.isEmpty())
                //                 Toast.makeText(this@AddDonaturActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    FancyToast.makeText(this, "Data Kosong!", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()

            }, Response.ErrorListener{ error ->
                srSponsor!!.isRefreshing = true

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

    fun deleteSponsor(id: Int){
        setLoading(true)

        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, SponsorApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var sponsor  = gson.fromJson(response, Sponsor:: class.java)


                if(sponsor != null)
                    FancyToast.makeText(this, "Data berhasil dihapus", FancyToast.LENGTH_SHORT,  FancyToast.SUCCESS, false).show()
                allSponsor()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allSponsor()
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