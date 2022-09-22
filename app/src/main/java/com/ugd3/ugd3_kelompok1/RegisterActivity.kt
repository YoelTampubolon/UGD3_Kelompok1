package com.ugd3.ugd3_kelompok1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerNamaLengkap: TextInputLayout
    private lateinit var registerEmail: TextInputLayout
    private lateinit var registerPassword: TextInputLayout
    private lateinit var registerTanggallahir: TextInputLayout
    private lateinit var registerNomorTelepon: TextInputLayout
    private lateinit var inputTextTanggalLahir : TextInputEditText
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
        inputTextTanggalLahir = findViewById(R.id.inputTextTanggalLahir)
        val btnDaftar: Button = findViewById(R.id.btnDaftar)
        val btnReset: Button = findViewById(R.id.btnReset)
        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        btnDaftar.setOnClickListener (View.OnClickListener{
            val mBundle = Bundle()
            var error = true

            val NamaLengkap: String = registerNamaLengkap.editText?.text.toString()
            val password: String = registerPassword.editText?.text.toString()
            val email: String = registerEmail.editText?.text.toString()
            val tanggalLahir: String = registerTanggallahir.editText?.text.toString()
            val NoTelpon: String = registerNomorTelepon.editText?.text.toString()

            mBundle.putString("username", registerNamaLengkap.editText?.text.toString())
            mBundle.putString("email", registerEmail.editText?.text.toString())
            mBundle.putString("password", registerPassword.editText?.text.toString())
            mBundle.putString("Tanggallahir", registerTanggallahir.editText?.text.toString())
            mBundle.putString("NoHandphone", registerNomorTelepon.editText?.text.toString())

            if(NamaLengkap.isEmpty()){
                registerNamaLengkap.setError("Nama Wajib Diisi")
                error = true
            }
            if(password.length!=8){
                registerPassword.setError("Password Wajib 8 Karakter")
                error = true
            }
            if(email.isEmpty()){
                registerEmail.setError("Email Wajib Diisi")
                error = true
            }
            if(tanggalLahir.isEmpty()){
                registerTanggallahir.setError("Tanggal Lahir Wajib Diisi")
                error = true
            }
            if(NoTelpon.isEmpty()){
                registerNomorTelepon.setError("No Telp Wajib Diisi")
                error = true
            }
            if(NamaLengkap.isNotEmpty() && password.length==8 && email.isNotEmpty() && tanggalLahir.isNotEmpty() && NoTelpon.isNotEmpty()) {
                error = false
                Snackbar.make(registerLayout, "Daftar Berhasil", Snackbar.LENGTH_LONG).show()


            }
            if(error)return@OnClickListener
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

        inputTextTanggalLahir.setOnFocusChangeListener { view, b ->
            val datePicker= DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                inputTextTanggalLahir.setText("${dayOfMonth}/${(month.toInt()+1).toString()}/${year}")

            },myYear, myMonth, myDay)

            if(b) {
                datePicker.show()
            }else {
                datePicker.hide()
            }
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