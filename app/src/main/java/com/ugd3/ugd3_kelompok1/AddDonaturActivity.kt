package com.ugd3.ugd3_kelompok1

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugd3.ugd3_kelompok1.Donasi.Constant
import com.ugd3.ugd3_kelompok1.Donasi.DonaturDB
import com.ugd3.ugd3_kelompok1.databinding.ActivityAddDonaturBinding
import kotlinx.android.synthetic.main.activity_add_donatur.*
import kotlinx.android.synthetic.main.fragment_donasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddDonaturActivity : AppCompatActivity() {

    val db by lazy {DonaturDB(this) }
    lateinit var donaturAdapter: DonaturAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donatur)
        supportActionBar?.hide()

        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        donaturAdapter = DonaturAdapter(arrayListOf(), object :
            DonaturAdapter.OnAdapterListener {
            override fun onClick(donaturs: Donaturs) {
                intentEdit(donaturs.id, Constant.TYPE_READ)
            }
            override fun onUpdate(donaturs: Donaturs) {
                intentEdit(donaturs.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(donaturs: Donaturs) {
                deleteDialog(donaturs)
            }
        })
        rv_donaturs.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = donaturAdapter
        }
    }

    private fun deleteDialog(donaturs: Donaturs){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${donaturs.name}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.donaturDao().deleteDonaturs(donaturs)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.donaturDao().getDonaturs()
            Log.d("AddDonaturActivity","dbResponse: $notes")
            withContext(Dispatchers.Main){
                donaturAdapter.setData( notes )
            }
        }
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteId : Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditDonaturActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }

}