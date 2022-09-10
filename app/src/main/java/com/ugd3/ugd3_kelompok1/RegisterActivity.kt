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
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerNamaLengkap: TextInputLayout
    private lateinit var registerEmail: TextInputLayout
    private lateinit var registerPassword: TextInputLayout
    private lateinit var registerTanggallahir: TextInputLayout
    private lateinit var registerNomorTelepon: TextInputLayout
    private lateinit var registerLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerNamaLengkap = findViewById(R.id.inputLayoutNama)
        registerEmail = findViewById(R.id.inputLayoutEmail)
        registerPassword = findViewById(R.id.inputLayoutPassword)
        registerTanggallahir = findViewById(R.id.inputLayoutTanggalLahir)
        registerNomorTelepon = findViewById(R.id.inputLayoutNomorTelepon)
        registerLayout = findViewById(R.id.activityRegister)
        val btnDaftar: Button = findViewById(R.id.btnDaftar)
        val btnReset: Button = findViewById(R.id.btnReset)

        btnDaftar.setOnClickListener (View.OnClickListener{
            val mBundle = Bundle()
            var error = true

            mBundle.putString("username", registerNamaLengkap.editText?.text.toString())
            mBundle.putString("email", registerEmail.editText?.text.toString())
            mBundle.putString("password", registerPassword.editText?.text.toString())
            mBundle.putString("Tanggallahir", registerTanggallahir.editText?.text.toString())
            mBundle.putString("NoHandphone", registerNomorTelepon.editText?.text.toString())

            Snackbar.make(registerLayout, "Daftar Berhasil", Snackbar.LENGTH_LONG).show()


            if(!error)return@OnClickListener
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.putExtra("register", mBundle)
            startActivity(intent)
        })

        btnReset.setOnClickListener{
            registerNamaLengkap.editText?.setText("")
            registerPassword.editText?.setText("")
            registerEmail.editText?.setText("")
            registerTanggallahir.editText?.setText("")
            registerNomorTelepon.editText?.setText("")

            Snackbar.make(registerLayout, "Sukses Mereset", Snackbar.LENGTH_LONG).show()
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
}