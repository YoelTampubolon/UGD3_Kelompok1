package com.ugd3.ugd3_kelompok1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Donate (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val namaLengkap: String,
    val email: String,
    val password: String,
    val tanggalLahir: String,
    val nomorTelepon: String
)