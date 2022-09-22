package com.ugd3.ugd3_kelompok1

import androidx.room.*

@Dao
interface DonateDao {

    @Insert
    suspend fun addDonate(donate: Donate)

    @Update
    suspend fun updateDonate(donate: Donate)

    @Delete
    suspend fun deleteDonate(donate: Donate)

    @Query("SELECT * FROM donate")
    suspend fun getDonates() : List<Donate>

    @Query("SELECT * FROM donate WHERE id =:donate_id")
    suspend fun getDonate(donate_id: Int) : List<Donate>

}