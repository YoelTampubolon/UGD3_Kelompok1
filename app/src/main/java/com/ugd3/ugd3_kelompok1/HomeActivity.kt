package com.ugd3.ugd3_kelompok1

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
    }

    fun changeFragment(fragment: Fragment?){
        if(fragment != null){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment,fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.nav_home){
            //Jika menu yang dipilih adalah Mahasiswa maka ganti fragmentnya dengan FragmentHome
            changeFragment(FragmentHome())
        }else if(item.itemId == R.id.nav_donasi){
            //Jika menu yang dipilih adalah Dosen maka ganti fragmentnya dengan FragmentDonasi
            changeFragment(FragmentDonatur())
        }else if(item.itemId == R.id.nav_profile)
            changeFragment(FragmentProfile())
        else {
            //Jika menu yang dipilih adalah menu Exit, maka tampilkan sebuah dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
            builder.setMessage("Apakah anda yakin ingin keluar?")
                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i : Int){
                        //Keluar dari aplikasi
                        finishAndRemoveTask()
                    }
                })
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

}