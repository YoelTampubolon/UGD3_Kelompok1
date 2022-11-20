package com.ugd3.ugd3_kelompok1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.ugd3.ugd3_kelompok1.Donasi.Constant
import com.ugd3.ugd3_kelompok1.Donasi.DonaturDB
import com.ugd3.ugd3_kelompok1.api.DonaturApi
import com.ugd3.ugd3_kelompok1.databinding.ActivityEditDonaturBinding
import com.ugd3.ugd3_kelompok1.models.Donatur
import kotlinx.android.synthetic.main.activity_edit_donatur.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditDonaturActivity : AppCompatActivity() {

//    val db by lazy { DonaturDB(this) }
//    private var noteId: Int = 0

    private lateinit var binding: ActivityEditDonaturBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var etName: EditText? = null
    private var etNominal: EditText? = null
    private var etAlamat: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_donatur)
//        binding = ActivityEditDonaturBinding.inflate(layoutInflater)
//        setContentView(binding!!.root)

        queue = Volley.newRequestQueue(this)
        etName = findViewById(R.id.et_nama)
        etNominal = findViewById(R.id.et_nominal)
        etAlamat = findViewById(R.id.et_alamat)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val id = intent.getIntExtra("id", -1)
        if(id == -1){
            btnSave.setOnClickListener { createDonatur() }
        }else{
            getDonaturByid(id)
            btnSave.setOnClickListener { updateDonatur(id) }
        }
        createNotificationChannel()

        supportActionBar?.hide()
//        setupView()
//        setupListener()
    }

    private fun getDonaturByid(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, DonaturApi.GET_BY_ID_URL + id, Response.Listener { response ->
                // val gson = Gson()
                // val mahasiswa = gson.fromJson(response, Mahasiswa::class.java)

                var jo = JSONObject(response.toString())
                val donatur = jo.getJSONObject("data")

                etName!!.setText(donatur.getString("name"))
                etNominal!!.setText(donatur.getString("nominal"))
                etAlamat!!.setText(donatur.getString("alamat"))

                Toast.makeText(this@EditDonaturActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditDonaturActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(this@EditDonaturActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createDonatur(){
        setLoading(true)

        val donatur = Donatur(0,
            etName!!.text.toString(),
            etNominal!!.text.toString(),
            etAlamat!!.text.toString(),

        )

        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, DonaturApi.ADD_URL, Response.Listener {response->
                val gson = Gson()
                val donatur = gson.fromJson(response, Donatur::class.java)

                if(donatur!=null)
                    Toast.makeText(this@EditDonaturActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditDonaturActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@EditDonaturActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers

                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(donatur)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    private fun updateDonatur(id: Int){
        setLoading(true)

        val donatur = Donatur(
            id,
            etName!!.text.toString(),
            etNominal!!.text.toString(),
            etAlamat!!.text.toString(),
        )


        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, DonaturApi.UPDATE_URL + id, Response.Listener {response->
                val gson = Gson()
                val donatur = gson.fromJson(response, Donatur::class.java)

                if(donatur!=null)
                    Toast.makeText(this@EditDonaturActivity, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditDonaturActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@EditDonaturActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(donatur)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1,name,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

//    fun setupView(){
//        val intentType = intent.getIntExtra("intent_type", 0)
//        when (intentType){
//            Constant.TYPE_CREATE -> {
//                button_update.visibility = View.GONE
//            }
//            Constant.TYPE_READ -> {
//                button_save.visibility = View.GONE
//                button_update.visibility = View.GONE
//                getNote()
//            }
//            Constant.TYPE_UPDATE -> {
//                button_save.visibility = View.GONE
//                getNote()
//            }
//        }
//    }
//
//    private fun setupListener() {
//        button_save.setOnClickListener {
//            sendNotification1()
//            CoroutineScope(Dispatchers.IO).launch {
//                db.donaturDao().addDonaturs(
//                    Donaturs(0,edit_nama.text.toString(),
//                        edit_nominal.text.toString(), edit_alamat.text.toString())
//                )
//                finish()
//            }
//        }
//        button_update.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                db.donaturDao().updateDonaturs(
//                    Donaturs(noteId,edit_nama.text.toString(),
//                        edit_nominal.text.toString(), edit_alamat.text.toString())
//                )
//                finish()
//            }
//        }
//    }

    private fun sendNotification1() {
        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_favorite)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setColor(Color.GREEN)
            .setContentTitle("Daftar Donatur")
            .setContentText("Ayo Mari Berdonasi")
//            Big Text  + Inbox Style
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Nama " +binding.etNama.text.toString())
                    .addLine("Nominal " + binding.etNominal.text.toString())
                    .addLine( "Alamat " + binding.etAlamat.text.toString())
                    .setBigContentTitle("Berhasil Tambah Data Donatur")
                    .setSummaryText("Rangkuman Text")
            )
        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }

//    fun getNote() {
//        noteId = intent.getIntExtra("intent_id", 0)
//        CoroutineScope(Dispatchers.IO).launch {
//            val notes = db.donaturDao().getDonatur(noteId)[0]
//            edit_nama.setText(notes.name)
//            edit_nominal.setText(notes.nominal)
//            edit_alamat.setText(notes.alamat)
//        }
//    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

}