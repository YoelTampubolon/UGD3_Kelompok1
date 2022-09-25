package com.ugd3.ugd3_kelompok1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_donatur)
        supportActionBar?.hide()
        setupView()
        setupListener()
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