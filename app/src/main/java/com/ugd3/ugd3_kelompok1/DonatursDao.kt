package com.ugd3.ugd3_kelompok1
import androidx.room.*

@Dao
interface DonatursDao {

    @Insert
    suspend fun addDonaturs(donaturs: Donaturs)

    @Update
    suspend fun updateDonaturs(donaturs: Donaturs)

    @Delete
    suspend fun deleteDonaturs(donaturs: Donaturs)

    @Query("SELECT * FROM donaturs")
    suspend fun getDonaturs() : List<Donaturs>

    @Query("SELECT * FROM donaturs WHERE id =:donaturs_id")
    suspend fun getDonatur(donaturs_id: Int) : List<Donaturs>


}