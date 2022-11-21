package com.ugd3.ugd3_kelompok1

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.ugd3.ugd3_kelompok1.Donasi.UserDB
import com.ugd3.ugd3_kelompok1.api.ProfileApi
import com.ugd3.ugd3_kelompok1.databinding.ActivityEditDonaturBinding
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*
import com.ugd3.ugd3_kelompok1.databinding.ActivityEditProfileBinding
import com.ugd3.ugd3_kelompok1.models.Profile
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var inputNama: TextInputEditText
    private lateinit var inputTanggalLahir : TextInputEditText
    private lateinit var inputNomorTelepon : TextInputEditText
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
//    private var layoutLoading: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_edit_profile)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        queue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        inputNama = binding.namaLengkapEdit
        inputTanggalLahir = binding.inputTextTanggalLahir
        inputNomorTelepon = binding.noTeleponEdit

//        inputNama = findViewById(R.id.inputLayoutNama)
//        inputTanggalLahir = findViewById(R.id.inputLayoutTanggalLahir)
//        inputNomorTelepon = findViewById(R.id.inputLayoutNomorTelepon)

        val btnUbah: Button = binding.btnUbah
        val btnBatal: Button = binding.btnBatal
//        val btnUbah: Button = findViewById(R.id.btnUbah)
//        val btnBatal: Button = findViewById(R.id.btnBatal)
//        val profileFragment = FragmentProfile()

//        val db by lazy { UserDB(this) }
//        val donateDao = db.donateDao()

        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences!!.getInt("id",0)

//        val user = donateDao.getDonate(sharedPreferences.getInt("id", 0))

        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        getProfileById(id)

        btnUbah.setOnClickListener(View.OnClickListener {
//            db.donateDao().updateDonate(
//                Donate(user.id, inputNama.editText?.text.toString(), user.email, user.password, inputTanggalLahir.editText?.text.toString(), inputNomorTelepon.editText?.text.toString() )
//            )
//            finish()
//            Toast.makeText(this@EditProfileActivity, "Edit berhasil",
//                Toast.LENGTH_LONG).show()
            updateProfile(id)
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

    private fun getProfileById(id: Int){
//        setLoading(true)

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ProfileApi.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response)
                    val json = jsonObject.getJSONObject("data")
                    val profile = Gson().fromJson(json.toString(), Profile::class.java)

                    inputNama!!.setText(profile.namaLengkap)
                    inputNomorTelepon!!.setText(profile.nomorTelepon)
                    inputTanggalLahir!!.setText(profile.tanggalLahir)

                    Toast.makeText(this@EditProfileActivity,"Data berhasil diambil", Toast.LENGTH_SHORT).show()
//                    setLoading(false)
                },
                Response.ErrorListener{ error ->
//                    setLoading(false)
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }

    private fun updateProfile(id: Int){
//        setLoading(true)
        val profile = Profile(
            inputNama!!.text.toString(),
            null,
            null,
            inputTanggalLahir!!.text.toString(),
            inputNomorTelepon!!.text.toString(),
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, ProfileApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var profile = gson.fromJson(response, Profile::class.java)

                if(profile != null){
                    sharedPreferences.edit()
                        .putString("nama", profile.namaLengkap)
                        .apply()
                    Toast.makeText(this@EditProfileActivity, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                }


                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

//                setLoading(false)
            }, Response.ErrorListener { error ->
//                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(profile)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

//    private fun setLoading(isLoading: Boolean){
//        if(isLoading){
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//          //  layoutLoading!!.visibility = View.INVISIBLE
//        }else{
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            layoutLoading!!.visibility = View.INVISIBLE
//        }
//    }

}
