package com.ugd3.ugd3_kelompok1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.ugd3.ugd3_kelompok1.databinding.ActivityRegisterBinding
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        binding.btnDaftar.setOnClickListener (View.OnClickListener{
            val mBundle = Bundle()
            var error = true

            val NamaLengkap: String = binding.inputLayoutNama.editText?.text.toString()
            val password: String = binding.inputLayoutPassword.editText?.text.toString()
            val email: String = binding.inputLayoutEmail.editText?.text.toString()
            val tanggalLahir: String = binding.inputLayoutTanggalLahir.editText?.text.toString()
            val NoTelpon: String = binding.inputLayoutNomorTelepon.editText?.text.toString()

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
                Snackbar.make(binding.activityRegister, "Daftar Berhasil", Snackbar.LENGTH_LONG).show()


            }
            if(error)return@OnClickListener
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