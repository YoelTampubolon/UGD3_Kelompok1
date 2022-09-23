package com.ugd3.ugd3_kelompok1

import androidx.room.*

@Dao
interface DonateDao {

    @Insert
    fun addDonate(donate: Donate)

    @Update
    fun updateDonate(donate: Donate)

    @Delete
    fun deleteDonate(donate: Donate)

    @Query("SELECT * FROM donate")
    fun getDonates() : List<Donate>

    @Query("SELECT * FROM donate WHERE id =:donate_id")
    fun getDonate(donate_id: Int) : Donate

    @Query("SELECT * FROM donate WHERE email = :email AND password = :password;")
    fun checkUser(email: String, password: String): Donate?

}