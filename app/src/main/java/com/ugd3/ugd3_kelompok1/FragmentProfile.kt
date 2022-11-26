package com.ugd3.ugd3_kelompok1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.ugd3.ugd3_kelompok1.Donasi.UserDB
import com.ugd3.ugd3_kelompok1.api.ProfileApi
import com.ugd3.ugd3_kelompok1.models.Profile
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.HashMap

class FragmentProfile : Fragment(){
    private lateinit var nama: TextView
    private lateinit var namaProfile: TextView
    private lateinit var email: TextView
    private lateinit var noHp: TextView
    private lateinit var tglLahir: TextView
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile ,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnEdit: Button = view.findViewById(R.id.editBtn)
//        val imageDelete : ImageView = view.findViewById(R.id.icon_delete)
        val imageCamera : ImageView = view.findViewById(R.id.iconCamera)
        val imagePerson : ImageView = view.findViewById(R.id.icon_person)
        queue = Volley.newRequestQueue(requireActivity())
//        val db by lazy { UserDB(activity as HomeActivity) }
//        val donateDao = db.donateDao()

        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences!!.getInt("id",0)
//        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

//        val user = donateDao.getDonate(sharedPreferences.getInt("id", 0))
        nama = view.findViewById(R.id.inputNama)
        namaProfile = view.findViewById(R.id.inputNamaProfile)
        email = view.findViewById(R.id.inputEmail)
        noHp = view.findViewById(R.id.inputPhonenumber)
        tglLahir = view.findViewById(R.id.inputTglLahir)

        getProfileById(id)
        // set text
//        nama.setText(user.namaLengkap)
//        namaProfile.setText(user.namaLengkap)
//        email.setText(user.email)
//        noHp.setText(user.nomorTelepon)
//        tglLahir.setText(user.tanggalLahir)

//        imageDelete.setOnClickListener{
//            donateDao.deleteDonate(user)
//            val moveDeleteImage = Intent(this@FragmentProfile.context, MainActivity::class.java)
//            startActivity(moveDeleteImage)
//        }

        imagePerson.setOnClickListener{
            val url ="https://placeimg.com/640/480/any"
            Glide.with(this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(icon_person)
        }

        imageCamera.setOnClickListener{
            val moveCamera = Intent(this@FragmentProfile.context, CameraActivity::class.java)
            startActivity(moveCamera)
        }

        btnEdit.setOnClickListener{
            val moveEditBtn = Intent(this@FragmentProfile.context, EditProfileActivity::class.java)
            startActivity(moveEditBtn)
        }


        btnLogout.setOnClickListener {
            val moveEditBtn = Intent(this@FragmentProfile.context, MainActivity::class.java)
            startActivity(moveEditBtn)
        }
    }
    private fun getProfileById(id: Int){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, ProfileApi.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response)
                    val json = jsonObject.getJSONObject("data")
                    val profile = Gson().fromJson(json.toString(), Profile::class.java)

                    nama!!.setText(profile.namaLengkap)
                    namaProfile!!.setText(profile.namaLengkap)
                    email!!.setText(profile.email)
                    noHp!!.setText(profile.nomorTelepon)
                    tglLahir!!.setText(profile.tanggalLahir)

                    Toast.makeText(requireActivity(),"Data berhasil diambil", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener{ error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            requireActivity(),
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
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
}
