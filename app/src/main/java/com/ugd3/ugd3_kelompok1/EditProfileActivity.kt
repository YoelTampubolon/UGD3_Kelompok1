package com.ugd3.ugd3_kelompok1

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.ugd3.ugd3_kelompok1.Donasi.UserDB
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*
import com.ugd3.ugd3_kelompok1.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var inputNama: TextInputLayout
    private lateinit var inputTanggalLahir : TextInputLayout
    private lateinit var inputNomorTelepon : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        inputNama = findViewById(R.id.inputLayoutNama)
        inputTanggalLahir = findViewById(R.id.inputLayoutTanggalLahir)
        inputNomorTelepon = findViewById(R.id.inputLayoutNomorTelepon)

        val btnUbah: Button = findViewById(R.id.btnUbah)
        val btnBatal: Button = findViewById(R.id.btnBatal)
        val profileFragment = FragmentProfile()

        val db by lazy { UserDB(this) }
        val donateDao = db.donateDao()

        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val user = donateDao.getDonate(sharedPreferences.getInt("id", 0))

        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        inputNama.editText?.setText(user.namaLengkap)
        inputNomorTelepon.editText?.setText(user.nomorTelepon)
        inputTanggalLahir.editText?.setText(user.tanggalLahir)

        btnUbah.setOnClickListener(View.OnClickListener {
            db.donateDao().updateDonate(
                Donate(user.id, inputNama.editText?.text.toString(), user.email, user.password, inputTanggalLahir.editText?.text.toString(), inputNomorTelepon.editText?.text.toString() )
            )
            finish()
            Toast.makeText(this@EditProfileActivity, "Edit berhasil",
                Toast.LENGTH_LONG).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        })

        inputTextTanggalLahir.setOnFocusChangeListener { view, b ->
            val datePicker= DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    inputTextTanggalLahir.setText("${dayOfMonth}/${(month.toInt()+1).toString()}/${year}")

                },myYear, myMonth, myDay)

            if(b) {
                datePicker.show()
            }else {
                datePicker.hide()
            }
        }

        btnBatal.setOnClickListener() {
            AlertDialog.Builder(this).apply {
                setMessage("Apakah anda ingin membatalkan?")

                setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }

                setNegativeButton("No"){_, _ ->

                }
                setCancelable(true)
            }.create().show()
        }
    }

}
