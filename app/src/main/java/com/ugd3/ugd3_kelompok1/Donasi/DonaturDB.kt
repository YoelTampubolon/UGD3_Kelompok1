package com.ugd3.ugd3_kelompok1.Donasi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ugd3.ugd3_kelompok1.Donaturs
import com.ugd3.ugd3_kelompok1.DonatursDao


@Database(
    entities = [Donaturs::class],
    version = 1
)

abstract class DonaturDB:RoomDatabase() {

    abstract fun donaturDao() : DonatursDao

    companion object {
        @Volatile private var instance : DonaturDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DonaturDB::class.java,
                "note12345.db"
            ).build()
    }

}