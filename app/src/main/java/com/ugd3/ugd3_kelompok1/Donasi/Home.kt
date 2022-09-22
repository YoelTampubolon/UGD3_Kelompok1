package com.ugd3.ugd3_kelompok1.Donasi

import com.ugd3.ugd3_kelompok1.R

class Home(var judulDonasi : String, var gambarDonasi : Int) {
    companion object {
        @JvmField
        var listOfHome = arrayOf(
            Home("Bangun Rumah Belajar Untuk Mereka", R.drawable.card1),
            Home("Bangun Rumah Belajar Untuk Mereka", R.drawable.card1),
            Home("Bangun Rumah Belajar Untuk Mereka", R.drawable.card1),
            Home("Bangun Rumah Belajar Untuk Mereka", R.drawable.card1),
            Home("Bangun Rumah Belajar Untuk Mereka", R.drawable.card1),
            )
    }
}