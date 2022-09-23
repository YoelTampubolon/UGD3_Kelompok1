package com.ugd3.ugd3_kelompok1.Donasi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ugd3.ugd3_kelompok1.Donate
import com.ugd3.ugd3_kelompok1.DonateDao

@Database (
    entities = [Donate::class],
    version = 1
)
abstract class UserDB: RoomDatabase() {

    abstract fun donateDao() : DonateDao
    companion object {
        @Volatile private var instance : UserDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "user.db"
        ).allowMainThreadQueries().build()
    }
}