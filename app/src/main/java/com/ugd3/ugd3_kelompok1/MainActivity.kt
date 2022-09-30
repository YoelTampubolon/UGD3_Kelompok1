package com.ugd3.ugd3_kelompok1

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.ugd3.ugd3_kelompok1.Donasi.UserDB
import com.ugd3.ugd3_kelompok1.databinding.ActivityEditDonaturBinding
import com.ugd3.ugd3_kelompok1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private val KEY_TEXT_REPLY = "key_text_reply"

    var mBundle : Bundle? = null
    var tempEmail : String = "admin"
    var tempPass : String = "admin"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        createNotificationChannel()

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)


        if(intent.getBundleExtra("register")!=null){
            mBundle = intent.getBundleExtra("register")
            tempEmail = mBundle!!.getString("email")!!
            tempPass = mBundle!!.getString("password")!!
            println(tempEmail)
            binding.inputLayoutEmailLogin.editText?.setText(tempEmail)
            binding.inputLayoutPasswordLogin.editText?.setText(tempPass)
        }

        binding.btnDaftar.setOnClickListener(View.OnClickListener  {
            val moveDaftar = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveDaftar)
        })

        binding.btnMasuk.setOnClickListener(View.OnClickListener {
            sendNotification1()
            var checkLogin = false
            binding.inputLayoutEmailLogin.setError(null)
            binding.inputLayoutPasswordLogin.setError(null)
            val email: String = binding.inputLayoutEmailLogin.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPasswordLogin.getEditText()?.getText().toString()

            if(email.isEmpty() || password.isEmpty()) {
                if(email.isEmpty()) {
                    binding.inputLayoutEmailLogin.setError("Email tidak boleh kosong")
                    checkLogin = false
                }else {
                    binding.inputLayoutPasswordLogin.setError("Password tidak boleh kosong")
                    checkLogin = false
                }

            }

            if(email.isEmpty() && password.isEmpty()) {
                binding.inputLayoutEmailLogin.setError("Email tidak boleh kosong")
                binding.inputLayoutPasswordLogin.setError("Password tidak boleh kosong")
                checkLogin = false
            }


            val db by lazy { UserDB(this) }
            val donateDao = db.donateDao()

            val user = donateDao.checkUser(email, password)
            if(user != null) {
                sharedPreferences.edit()
                    .putInt("id", user.id)
                    .apply()

                checkLogin = true
            } else {
                Snackbar.make(binding.loginLayout, "Login gagal! Username atau password salah", Snackbar.LENGTH_LONG).show()
            }
            if(!checkLogin) return@OnClickListener
            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })
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

    private fun sendNotification1(){
        val resultsIntent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val replyLabel = "Enter your reply"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel)
            .build()

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultsIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val icon = Icon.createWithResource(this@MainActivity,
            android.R.drawable.ic_dialog_info)

        val replyAction = Notification.Action.Builder(
            icon,
            "Reply", resultPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()
    }
}