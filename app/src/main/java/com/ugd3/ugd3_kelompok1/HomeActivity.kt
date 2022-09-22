package com.ugd3.ugd3_kelompok1

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.PopupMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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


//
//        navController = findNavController(R.id.layout_fragment)
//        setupActionBarWithNavController(navController)
//        setupSmoothBottomMenu()

    }

    private fun setThatFragments(fragment : Fragment){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.layout_fragment,fragment)
                commit()
            }
    }



////    Method untuk mengubah fragment
//    fun changeFragment(fragment: Fragment?){
//        if(fragment != null){
//            getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.layout_fragment,fragment)
//                .commit()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        //Disini kita menghubungkan menu yang telah kita buat dengan activity ini
//        val menuInflater = MenuInflater(this)
//        menuInflater.inflate(R.menu.home_navigation, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.nav_home){
//            //Jika menu yang dipilih adalah home maka ganti fragmentnya dengan FragmentHome
//            changeFragment(FragmentHome())
//        }else if(item.itemId == R.id.nav_donasi){
//            //Jika menu yang dipilih adalah donatur maka ganti fragmentnya dengan FragmentDonasi
//            changeFragment(FragmentDonasi())
//        }else if(item.itemId == R.id.nav_profile)
//            changeFragment(FragmentProfile())
//        else {
//            //Jika menu yang dipilih adalah menu Exit, maka tampilkan sebuah dialog
//            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
//            builder.setMessage("Apakah anda yakin ingin keluar?")
//                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
//                    override fun onClick(dialogInterface: DialogInterface, i : Int){
//                        //Keluar dari aplikasi
//                        finishAndRemoveTask()
//                    }
//                })
//                .show()
//        }
//        return super.onOptionsItemSelected(item)
//    }

}