package com.ugd3.ugd3_kelompok1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerNamaLengkap: TextInputEditText
    private lateinit var registerEmail: TextInputEditText
    private lateinit var registerPassword: TextInputEditText
    private lateinit var registerTanggallahir: TextInputEditText
    private lateinit var registerNomorTelepon: TextInputEditText
    private lateinit var registerLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        println("masuk")
        registerNamaLengkap = findViewById(R.id.namaLengkap)
        registerEmail = findViewById(R.id.email)
        registerPassword = findViewById(R.id.password)
        registerTanggallahir = findViewById(R.id.tanggalLahir)
        registerNomorTelepon = findViewById(R.id.nomorTelepon)
        registerLayout = findViewById(R.id.activityRegister)
        val btnDaftar: Button = findViewById(R.id.btnDaftar)
        val btnReset: Button = findViewById(R.id.btnReset)
        println("masuk2")

        btnDaftar.setOnClickListener (View.OnClickListener{
            val mBundle = Bundle()
            var error = true

            mBundle.putString("username", registerNamaLengkap.text.toString())
            mBundle.putString("email", registerEmail.text.toString())
            mBundle.putString("password", registerPassword.text.toString())
            mBundle.putString("Tanggallahir", registerTanggallahir.text.toString())
            mBundle.putString("NoHandphone", registerNomorTelepon.text.toString())

            Snackbar.make(registerLayout, "Daftar Berhasil", Snackbar.LENGTH_LONG).show()


            if(!error)return@OnClickListener
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.putExtra("register", mBundle)
            startActivity(intent)
        })

        btnReset.setOnClickListener{
            registerNamaLengkap.setText("")
            registerPassword.setText("")
            registerEmail.setText("")
            registerTanggallahir.setText("")
            registerNomorTelepon.setText("")

            Snackbar.make(registerLayout, "Sukses Mereset", Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }

            setNegativeButton("No"){_, _ ->
                Toast.makeText(this@RegisterActivity, "Terima Kasih",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }
}