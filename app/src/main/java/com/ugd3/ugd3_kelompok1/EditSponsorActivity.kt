package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import com.ugd3.ugd3_kelompok1.api.SponsorApi
import com.ugd3.ugd3_kelompok1.models.Sponsor
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditSponsorActivity : AppCompatActivity() {
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    private var etNamaSponsor:EditText? = null
    private var etUsia: EditText? = null
    private var etNominal: EditText? = null
    private var etTujuanDonasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sponsor)

        queue = Volley.newRequestQueue(this)
        etNamaSponsor = findViewById(R.id.et_namaSponsor)
        etUsia = findViewById(R.id.et_usia)
        etNominal = findViewById(R.id.et_nominal)
        etTujuanDonasi= findViewById(R.id.et_tujuanDonasi)
        layoutLoading = findViewById(R.id.layout_loading)


        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val id = intent.getIntExtra("id", -1)
        if(id == -1){
            btnSave.setOnClickListener {
                val namaSponsor = etNamaSponsor!!.text.toString()
                val usia = etUsia!!.text.toString()
                val nominal = etNominal!!.text.toString()
                val tujuanDonasi = etTujuanDonasi!!.text.toString()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (namaSponsor.isEmpty() && usia.isEmpty() && nominal.isEmpty() && tujuanDonasi.isEmpty()){
                            FancyToast.makeText(applicationContext,"Semuanya Tidak boleh Kosong" , FancyToast.LENGTH_SHORT,  FancyToast.INFO, false).show()
                        }else {
                            createSponsor()
                        }

                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }

        }else{
            getSponsorByid(id)
            btnSave.setOnClickListener { updateSponsor(id) }
        }
        createNotificationChannel()

        supportActionBar?.hide()
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )

    private fun getSponsorByid(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, SponsorApi.GET_BY_ID_URL + id, Response.Listener { response ->

                var jo = JSONObject(response.toString())
                val Sponsor = jo.getJSONObject("data")

                etNamaSponsor!!.setText(Sponsor.getString("namaSponsor"))
                etUsia!!.setText(Sponsor.getString("usia"))
                etNominal!!.setText(Sponsor.getString("nominal"))
                etTujuanDonasi!!.setText(Sponsor.getString("tujuanDonasi"))

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


    private fun createSponsor(){
        setLoading(true)

        if(etNamaSponsor!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditSponsorActivity, "Nama Sponsor tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etUsia!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditSponsorActivity, "Usia tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etNominal!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditSponsorActivity, "Nominal tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etTujuanDonasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditSponsorActivity, "Tujuan Donasi tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else {
            val Sponsor = Sponsor(
                0,
                etNamaSponsor!!.text.toString(),
                etUsia!!.text.toString(),
                etNominal!!.text.toString(),
                etTujuanDonasi!!.text.toString(),
            )

            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, SponsorApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val Sponsor = gson.fromJson(response, Sponsor::class.java)

                        if (Sponsor != null)
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
                        val requestBody = gson.toJson(Sponsor)
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

    private fun updateSponsor(id: Int){
        setLoading(true)

        val Sponsor = Sponsor(
            id,
            etNamaSponsor!!.text.toString(),
            etUsia!!.text.toString(),
            etNominal!!.text.toString(),
            etTujuanDonasi!!.text.toString(),
        )


        val stringRequest: StringRequest =
            object : StringRequest(
                Method.PUT,
                SponsorApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val Sponsor = gson.fromJson(response, Sponsor::class.java)

                    if (Sponsor != null)
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
                            this@EditSponsorActivity,
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
                    val requestBody = gson.toJson(Sponsor)
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