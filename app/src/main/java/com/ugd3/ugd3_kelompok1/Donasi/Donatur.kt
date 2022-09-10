package com.ugd3.ugd3_kelompok1.Donasi

class Donatur (var name: String,var nominal: Int, var alamat: String){
//    Kelas buat nampilin recycler view

    companion object{
        @JvmField
        var listOfDonatur = arrayOf(
            Donatur("Kuswari",5000,"Jalan Tambak Bayan 1"),
            Donatur("Jeremy Sukaci",6000,"Jalan Tambak Bayan 2"),
            Donatur("Ace",7000,"Jalan Tambak Bayan 3"),
            Donatur("Anitaa",25000,"Jalan Tambak Bayan 4"),
            Donatur("Bharada Joel",8000,"Jalan Tambak Bayan 5"),
            Donatur("Dyo Sambo",9000,"Jalan Tambak Bayan 6"),
            Donatur("Erik Bayu",10000,"Jalan Tambak Bayan 7"),
            Donatur("Tangkas Island Boy",11000,"Jalan Tambak Bayan 8"),
            Donatur("Wisnawi",12000,"Jalan Tambak Bayan 9"),
            Donatur("Pray Simatupang",13000,"Jalan Tambak Bayan 10"),
            )
    }
}