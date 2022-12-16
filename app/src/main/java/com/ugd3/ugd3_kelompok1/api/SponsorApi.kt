package com.ugd3.ugd3_kelompok1.api

class SponsorApi {
    companion object {
        val BASE_URL = "http://192.168.100.118/UGD_Kelompok1/public/api/"

        val GET_ALL_URL = BASE_URL + "sponsor"
        val GET_BY_ID_URL = BASE_URL + "sponsor/"
        val ADD_URL = BASE_URL + "sponsor"
        val UPDATE_URL = BASE_URL + "sponsor/"
        val DELETE_URL = BASE_URL + "sponsor/"
    }
}