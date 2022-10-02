package com.ugd3.ugd3_kelompok1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ugd3.ugd3_kelompok1.Donasi.Constant
import com.ugd3.ugd3_kelompok1.Donasi.DonaturDB
import com.ugd3.ugd3_kelompok1.databinding.ActivityEditDonaturBinding
import kotlinx.android.synthetic.main.activity_add_donatur.*
import kotlinx.android.synthetic.main.activity_edit_donatur.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditDonaturActivity : AppCompatActivity() {

    val db by lazy { DonaturDB(this) }
    private var noteId: Int = 0

    private lateinit var binding: ActivityEditDonaturBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_donatur)
        binding = ActivityEditDonaturBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        createNotificationChannel()

        supportActionBar?.hide()
        setupView()
        setupListener()
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

    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getNote()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }

    private fun setupListener() {
        button_save.setOnClickListener {
            sendNotification1()
            CoroutineScope(Dispatchers.IO).launch {
                db.donaturDao().addDonaturs(
                    Donaturs(0,edit_nama.text.toString(),
                        edit_nominal.text.toString(), edit_alamat.text.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.donaturDao().updateDonaturs(
                    Donaturs(noteId,edit_nama.text.toString(),
                        edit_nominal.text.toString(), edit_alamat.text.toString())
                )
                finish()
            }
        }
    }

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
                    .addLine("Nama " +binding.editNama.text.toString())
                    .addLine("Nominal " + binding.editNominal.text.toString())
                    .addLine( "Alamat " + binding.editAlamat.text.toString())
                    .setBigContentTitle("Berhasil Tambah Data Donatur")
                    .setSummaryText("Rangkuman Text")
            )
        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }

    fun getNote() {
        noteId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.donaturDao().getDonatur(noteId)[0]
            edit_nama.setText(notes.name)
            edit_nominal.setText(notes.nominal)
            edit_alamat.setText(notes.alamat)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}