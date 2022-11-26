package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.itextpdf.layout.Document
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.shashank.sony.fancytoastlib.FancyToast


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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditDonaturActivity : AppCompatActivity() {

//    val db by lazy { DonaturDB(this) }
//    private var noteId: Int = 0

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
            btnSave.setOnClickListener {
                val nama = etName!!.text.toString()
                val nominal = etNominal!!.text.toString()
                val alamat = etAlamat!!.text.toString()
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (nama.isEmpty() && nominal.isEmpty() && alamat.isEmpty()){
                            FancyToast.makeText(applicationContext,"Semuanya Tidak boleh Kosong" , FancyToast.LENGTH_SHORT,  FancyToast.INFO, false).show()
                        }else {
                            createDonatur()
                            createPdf(nama, nominal, alamat)
                        }

                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }

        }else{
            getDonaturByid(id)
            btnSave.setOnClickListener { updateDonatur(id) }
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
private fun createPdf(nama: String, nominal: String, alamat: String) {
    val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    val file = File(pdfPath, "File Donatur.pdf")
    FileOutputStream(file)


    val writer = PdfWriter(file)
    val pdfDocument = PdfDocument(writer)
    val document = Document(pdfDocument)
    pdfDocument.defaultPageSize = PageSize.A4
    document.setMargins(5f, 5f, 5f, 40f)
    @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.komunitas)


    val bitmap = (d as BitmapDrawable?)!!.bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val bitmapData = stream.toByteArray()
    val imageData = ImageDataFactory.create(bitmapData)
    val image = Image(imageData)
    val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
        .setTextAlignment(TextAlignment.CENTER)
    val group = Paragraph(
        """
                        Berikut adalah
                        Nama Donatur 
                        """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)


    val width = floatArrayOf(100f, 100f)
    val table = Table(width)

    table.setHorizontalAlignment(HorizontalAlignment.CENTER)
    table.addCell(Cell().add(Paragraph("Nama")))
    table.addCell(Cell().add(Paragraph(nama)))
    table.addCell(Cell().add(Paragraph("Nominal")))
    table.addCell(Cell().add(Paragraph(nominal)))
    table.addCell(Cell().add(Paragraph("Alamat")))
    table.addCell(Cell().add(Paragraph(alamat)))
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
    table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
    table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
    table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))


    val barcodeQRCode = BarcodeQRCode(
        """
                                        $nama
                                        $nominal
                                        $alamat
                                        ${LocalDate.now().format(dateTimeFormatter)}
                                        ${LocalTime.now().format(timeFormatter)}
                                        """.trimIndent())
    val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
    val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

    document.add(image)
    document.add(namapengguna)
    document.add(group)
    document.add(table)
    document.add(qrCodeImage)


    document.close()
    FancyToast.makeText(this, "Pdf Created", FancyToast.LENGTH_LONG,  FancyToast.DEFAULT, false).show()
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
                    FancyToast.makeText(this, "Data Berhasil Ditambahkan", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch (e:Exception){
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
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
                    FancyToast.makeText(this, "Data Berhasil Diupdate", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch (e:Exception){
                    FancyToast.makeText(this@EditDonaturActivity, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
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

//    private fun sendNotification1() {
//        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
//            .setSmallIcon(R.drawable.ic_favorite)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setColor(Color.GREEN)
//            .setContentTitle("Daftar Donatur")
//            .setContentText("Ayo Mari Berdonasi")
////            Big Text  + Inbox Style
//            .setStyle(
//                NotificationCompat.InboxStyle()
//                    .addLine("Nama " +binding.etNama.text.toString())
//                    .addLine("Nominal " + binding.etNominal.text.toString())
//                    .addLine( "Alamat " + binding.etAlamat.text.toString())
//                    .setBigContentTitle("Berhasil Tambah Data Donatur")
//                    .setSummaryText("Rangkuman Text")
//            )
//        with(NotificationManagerCompat.from(this)){
//            notify(notificationId1, builder.build())
//        }
//    }

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