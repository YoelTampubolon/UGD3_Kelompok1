package com.ugd3.ugd3_kelompok1

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ugd3.ugd3_kelompok1.api.ProfileApi
import com.ugd3.ugd3_kelompok1.databinding.ActivityRegisterBinding
import com.ugd3.ugd3_kelompok1.models.Profile
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null
    private lateinit var NamaLengkap: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var tanggalLahir: String
    private lateinit var NoTelpon: String
    private var layoutLoading: LinearLayout? = null
//    private val KEY_TEXT_REPLY = "key_text_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createNotificationChannel()

//        val db by lazy { UserDB(this) }
//        val donateDao = db.donateDao()
        queue = Volley.newRequestQueue(this)

        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        binding.btnDaftar.setOnClickListener (View.OnClickListener{
//          Notification
            sendNotification1()

            val mBundle = Bundle()
            var error = true
//
            NamaLengkap = binding.inputLayoutNama.editText?.text.toString()
            password = binding.inputLayoutPassword.editText?.text.toString()
            email = binding.inputLayoutEmail.editText?.text.toString()
            tanggalLahir = binding.inputLayoutTanggalLahir.editText?.text.toString()
            NoTelpon = binding.inputLayoutNomorTelepon.editText?.text.toString()
//
            mBundle.putString("username", binding.inputLayoutNama.editText?.text.toString())
            mBundle.putString("email",    binding.inputLayoutEmail.editText?.text.toString())
            mBundle.putString("password", binding.inputLayoutPassword .editText?.text.toString())
            mBundle.putString("Tanggallahir", binding.inputLayoutTanggalLahir.editText?.text.toString())
            mBundle.putString("NoHandphone", binding.inputLayoutNomorTelepon.editText?.text.toString())

            if(NamaLengkap.isEmpty()){
                binding.inputLayoutNama.setError("Nama Wajib Diisi")
                error = true
            }
            if(password.length!=8){
                binding.inputLayoutPassword.setError("Password Wajib 8 Karakter")
                error = true
            }
            if(email.isEmpty()){
                binding.inputLayoutEmail.setError("Email Wajib Diisi")
                error = true
            }
            if(tanggalLahir.isEmpty()){
                binding.inputLayoutTanggalLahir.setError("Tanggal Lahir Wajib Diisi")
                error = true
            }
            if(NoTelpon.isEmpty()){
                binding.inputLayoutNomorTelepon.setError("No Telp Wajib Diisi")
                error = true
            }
            if(NamaLengkap.isNotEmpty() && password.length==8 && email.isNotEmpty() && tanggalLahir.isNotEmpty() && NoTelpon.isNotEmpty()) {
                error = false
                createProfile()
                Snackbar.make(binding.activityRegister, "Daftar Berhasil", Snackbar.LENGTH_LONG).show()
            }
            if(error)return@OnClickListener

//            val users = donateDao.getDonates()
//            println(users)
            // Add user to database
//            val donate = Donate(0, NamaLengkap, email, password, tanggalLahir, NoTelpon)
//            donateDao.addDonate(donate)

            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.putExtra("register", mBundle)
            startActivity(intent)
        })

        binding.btnReset.setOnClickListener{
            binding.inputLayoutNama.editText?.setText("")
            binding.inputLayoutPassword.editText?.setText("")
            binding.inputLayoutEmail.editText?.setText("")
            binding.inputLayoutTanggalLahir.editText?.setText("")
            binding.inputLayoutNomorTelepon.editText?.setText("")

            Snackbar.make(binding.activityRegister, "Sukses Mereset", Snackbar.LENGTH_LONG).show()
        }

        binding.inputTextTanggalLahir.setOnFocusChangeListener { view, b ->
            val datePicker= DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.inputTextTanggalLahir.setText("${dayOfMonth}/${(month.toInt()+1).toString()}/${year}")

            },myYear, myMonth, myDay)

            if(b) {
                datePicker.show()
            }else {
                datePicker.hide()
            }
        }
    }

    private fun createNotificationChannel(){
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

    private fun sendNotification1(){
        val intent: Intent = Intent(this, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage","Selamat Datang " + binding.inputLayoutNama.editText?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val picture = BitmapFactory.decodeResource(resources,R.drawable.logo)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
            .setSmallIcon(R.drawable.logo)
            .setContentText("Berhasil Register")
//                Big Picture Style
            .setLargeIcon(picture)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null)
                .bigPicture(picture))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1,builder.build())
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
                Toast.makeText(this@RegisterActivity, "Terima Kasih",
                    Toast.LENGTH_LONG).show()
            }

            setNegativeButton("No"){_, _ ->

            }

            setCancelable(true)
        }.create().show()
    }

    private fun createProfile(){
//        setLoading(true)
        val profile = Profile(
            NamaLengkap,
            email,
            password,
            tanggalLahir,
            NoTelpon,
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, ProfileApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val json = jsonObject.getJSONObject("data")
                var profile = gson.fromJson(json.toString(), Profile::class.java)

                if(profile != null)
                    Toast.makeText(this@RegisterActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
//                println(profile[0].namaLengkap)

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

//                setLoading(false)
            }, Response.ErrorListener { error ->
//                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(profile)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }
//    private fun setLoading(isLoading: Boolean){
//        if(isLoading){
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//            layoutLoading!!.visibility = View.INVISIBLE
//        }else{
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            layoutLoading!!.visibility = View.INVISIBLE
//        }
//    }
}