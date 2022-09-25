package com.ugd3.ugd3_kelompok1

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Donaturs (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val nominal: String,
    val alamat: String
)