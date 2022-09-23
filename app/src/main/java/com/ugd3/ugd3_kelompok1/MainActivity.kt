package com.ugd3.ugd3_kelompok1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.ugd3.ugd3_kelompok1.Donasi.UserDB

class MainActivity : AppCompatActivity() {
    private lateinit var inputEmail : TextInputLayout
    private lateinit var inputPassword : TextInputLayout
    private lateinit var loginLayout : ConstraintLayout
    var mBundle : Bundle? = null
    var tempEmail : String = "admin"
    var tempPass : String = "admin"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        inputEmail = findViewById(R.id.inputLayoutEmailLogin)
        inputPassword = findViewById(R.id.inputLayoutPasswordLogin)
        loginLayout = findViewById(R.id.loginLayout)
        val btnMasuk: Button = findViewById(R.id.btnMasuk)
        val btnDaftar: Button = findViewById(R.id.btnDaftar)

        if(intent.getBundleExtra("register")!=null){
            mBundle = intent.getBundleExtra("register")
            tempEmail = mBundle!!.getString("email")!!
            tempPass = mBundle!!.getString("password")!!
            println(tempEmail)
            inputEmail.editText?.setText(tempEmail)
            inputPassword.editText?.setText(tempPass)
        }

        btnDaftar.setOnClickListener(View.OnClickListener  {
            val moveDaftar = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveDaftar)
        })

        btnMasuk.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            inputEmail.setError(null)
            inputPassword.setError(null)
            val email: String = inputEmail.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(email.isEmpty() || password.isEmpty()) {
                if(email.isEmpty()) {
                    inputEmail.setError("Email tidak boleh kosong")
                    checkLogin = false
                }else {
                    inputPassword.setError("Password tidak boleh kosong")
                    checkLogin = false
                }

            }

            if(email.isEmpty() && password.isEmpty()) {
                inputEmail.setError("Email tidak boleh kosong")
                inputPassword.setError("Password tidak boleh kosong")
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
                Snackbar.make(loginLayout, "Login gagal! Username atau password salah", Snackbar.LENGTH_LONG).show()
            }
            if(!checkLogin) return@OnClickListener
            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })
    }
}