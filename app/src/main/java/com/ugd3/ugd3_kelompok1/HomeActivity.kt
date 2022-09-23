package com.ugd3.ugd3_kelompok1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        getSupportActionBar()?.hide()
        var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        val homeFragment = FragmentHome()
        val donasiFragment = FragmentDonasi()
        val profileFragment = FragmentProfile()

        setThatFragments(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home ->{
                    setThatFragments(homeFragment)
                }
                R.id.nav_donasi ->{
                    setThatFragments(donasiFragment)
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