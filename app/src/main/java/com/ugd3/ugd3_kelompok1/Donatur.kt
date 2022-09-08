package com.ugd3.ugd3_kelompok1

class Donatur (var name: String,var nominal: Double, var alamat: String){
//    Kelas buat nampilin recycler view

    companion object{
        @JvmField
        var listOfDonatur = arrayOf(
            Donatur("Kuswari",5.000,"Jalan Tambak Bayan 1"),
            Donatur("Jeremy Sukaci",6.000,"Jalan Tambak Bayan 2"),
            Donatur("Ace",7.000,"Jalan Tambak Bayan 3"),
            Donatur("Anitaa",25.000,"Jalan Tambak Bayan 4"),
            Donatur("Bharada Joel",8.000,"Jalan Tambak Bayan 5"),
            Donatur("Dyo Sambo",9.000,"Jalan Tambak Bayan 6"),
            Donatur("Erik Bayu",10.000,"Jalan Tambak Bayan 7"),
            Donatur("Tangkas Island Boy",11.000,"Jalan Tambak Bayan 8"),
            Donatur("Wisnawi",12.000,"Jalan Tambak Bayan 9"),
            Donatur("Pray Simatupang",13.000,"Jalan Tambak Bayan 10"),
            )
    }
}