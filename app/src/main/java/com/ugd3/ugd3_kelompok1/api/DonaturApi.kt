package com.ugd3.ugd3_kelompok1.api

class DonaturApi {
    companion object {
        val BASE_URL = "http://10.53.9.13/UGD_Kelompok1/public/api/"

        val GET_ALL_URL = BASE_URL + "donatur"
        val GET_BY_ID_URL = BASE_URL + "donatur/"
        val ADD_URL = BASE_URL + "donatur"
        val UPDATE_URL = BASE_URL + "donatur/"
        val DELETE_URL = BASE_URL + "donatur/"
    }
}