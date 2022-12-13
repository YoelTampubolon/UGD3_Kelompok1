package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.shashank.sony.fancytoastlib.FancyToast
import com.ugd3.ugd3_kelompok1.api.PenyelenggaraApi
import com.ugd3.ugd3_kelompok1.models.Penyelenggara
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditPenyelenggaraActivity : AppCompatActivity() {
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var etNamaPenyelenggara: EditText? = null
    private var etJudulDonasi: EditText? = null
    private var etKategoriDonasi: EditText? = null
    private var etTargetJumlahDonasi: EditText? = null
    private var etBatasWaktuDonasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_penyelenggara)
//        binding = ActivityEditDonaturBinding.inflate(layoutInflater)
//        setContentView(binding!!.root)

        queue = Volley.newRequestQueue(this)
        etNamaPenyelenggara = findViewById(R.id.et_namaPenyelenggara)
        etJudulDonasi = findViewById(R.id.et_judul)
        etKategoriDonasi = findViewById(R.id.et_kategori)
        etTargetJumlahDonasi = findViewById(R.id.et_target)
        etBatasWaktuDonasi = findViewById(R.id.et_batas)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val id = intent.getIntExtra("id", -1)
        if(id == -1){
            btnSave.setOnClickListener {
                val namaPenyelenggara = etNamaPenyelenggara!!.text.toString()
                val judul = etJudulDonasi!!.text.toString()
                val kategori = etKategoriDonasi!!.text.toString()
                val target = etTargetJumlahDonasi!!.text.toString()
                val batas = etBatasWaktuDonasi!!.text.toString()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (namaPenyelenggara.isEmpty() && judul.isEmpty() && kategori.isEmpty() && target.isEmpty() && batas.isEmpty()){
                            FancyToast.makeText(applicationContext,"Semuanya Tidak boleh Kosong" , FancyToast.LENGTH_SHORT,  FancyToast.INFO, false).show()
                        }else {
                            createPenyelenggara()
                        }

                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }

        }else{
            getPenyelenggaraByid(id)
            btnSave.setOnClickListener { updatePenyelenggara(id) }
        }
        createNotificationChannel()

        supportActionBar?.hide()
//        setupView()
//        setupListener()
    }
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )

    private fun getPenyelenggaraByid(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PenyelenggaraApi.GET_BY_ID_URL + id, Response.Listener { response ->
                // val gson = Gson()
                // val mahasiswa = gson.fromJson(response, Mahasiswa::class.java)

                var jo = JSONObject(response.toString())
                val penyelenggara = jo.getJSONObject("data")

                etNamaPenyelenggara!!.setText(penyelenggara.getString("namaPenyelenggara"))
                etJudulDonasi!!.setText(penyelenggara.getString("judulDonasi"))
                etKategoriDonasi!!.setText(penyelenggara.getString("kategoriDonasi"))
                etTargetJumlahDonasi!!.setText(penyelenggara.getString("targetJumlahDonasi"))
                etBatasWaktuDonasi!!.setText(penyelenggara.getString("batasWaktuDonasi"))

                FancyToast.makeText(this, "Data berhasil diambil!", FancyToast.LENGTH_SHORT,  FancyToast.SUCCESS, false).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch (e: Exception) {
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
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

    private fun createPenyelenggara(){
        setLoading(true)


        if(etNamaPenyelenggara!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPenyelenggaraActivity, "Nama Penyelenggara tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etJudulDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPenyelenggaraActivity, "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etKategoriDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPenyelenggaraActivity, "Kategori tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etTargetJumlahDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPenyelenggaraActivity, "Target Jumlah Donasi tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etBatasWaktuDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditPenyelenggaraActivity, "Batas Waktu tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else {
            val penyelenggara = Penyelenggara(
                0,
                etNamaPenyelenggara!!.text.toString(),
                etJudulDonasi!!.text.toString(),
                etKategoriDonasi!!.text.toString(),
                etTargetJumlahDonasi!!.text.toString(),
                etBatasWaktuDonasi!!.text.toString(),
            )

            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, PenyelenggaraApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val penyelenggara = gson.fromJson(response, Penyelenggara::class.java)

                        if (penyelenggara != null)
                            FancyToast.makeText(
                                this,
                                "Data Berhasil Ditambahkan",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                false
                            ).show()

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                        setLoading(false)
                    }, Response.ErrorListener { error ->
                        setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            FancyToast.makeText(
                                this,
                                errors.getString("message"),
                                FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                            ).show()
                        } catch (e: Exception) {
                            FancyToast.makeText(
                                this,
                                e.message,
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                            ).show()
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
                        val requestBody = gson.toJson(penyelenggara)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            // Menambahkan request ke request queue
            queue!!.add(stringRequest)
        }
        setLoading(false)
    }

    private fun updatePenyelenggara(id: Int){
        setLoading(true)

        val penyelenggara = Penyelenggara(
            id,
            etNamaPenyelenggara!!.text.toString(),
            etJudulDonasi!!.text.toString(),
            etKategoriDonasi!!.text.toString(),
            etTargetJumlahDonasi!!.text.toString(),
            etBatasWaktuDonasi!!.text.toString(),
        )


        val stringRequest: StringRequest =
            object : StringRequest(
                Method.PUT,
                PenyelenggaraApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val penyelenggara = gson.fromJson(response, Penyelenggara::class.java)

                    if (penyelenggara != null)
                        FancyToast.makeText(
                            this,
                            "Data Berhasil Diupdate",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,
                            false
                        ).show()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        FancyToast.makeText(
                            this,
                            errors.getString("message"),
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                        ).show()
                    } catch (e: Exception) {
                        FancyToast.makeText(
                            this@EditPenyelenggaraActivity,
                            e.message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
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
                    val requestBody = gson.toJson(penyelenggara)
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