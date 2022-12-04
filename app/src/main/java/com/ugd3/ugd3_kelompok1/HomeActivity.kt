package com.ugd3.ugd3_kelompok1

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
//     private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        getSupportActionBar()?.hide()

//        var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        val homeFragment = FragmentHome()
        val donasiFragment = FragmentDonasi()
        val profileFragment = FragmentProfile()
        val lokasiFragment = FragmentLocation()
        val qrFragment = FragmentQR()
//        init()
        setThatFragments(FragmentHome())
//        navListener()

        nav_view.setOnItemSelectedListener {
            when(it){
                R.id.nav_home ->{
                    setThatFragments(homeFragment)
                }
                R.id.nav_donasi ->{
                    setThatFragments(donasiFragment)
                }
                R.id.nav_qr ->{
                    setThatFragments(qrFragment)
                }
                R.id.nav_lokasi ->{
                    setThatFragments(lokasiFragment)
                }
                R.id.nav_profile ->{
                    setThatFragments(profileFragment)
                }
            }
            true
        }
    }



    private fun setThatFragments(fragment : Fragment){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.layout_fragment,fragment)
                commit()
            }
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

}