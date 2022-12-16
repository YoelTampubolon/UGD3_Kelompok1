package com.ugd3.ugd3_kelompok1.api

class PenyelenggaraApi {
    companion object {
        val BASE_URL = "http://192.168.100.118/UGD_Kelompok1/public/api/"

        val GET_ALL_URL = BASE_URL + "penyelenggara"
        val GET_BY_ID_URL = BASE_URL + "penyelenggara/"
        val ADD_URL = BASE_URL + "penyelenggara"
        val UPDATE_URL = BASE_URL + "penyelenggara/"
        val DELETE_URL = BASE_URL + "penyelenggara/"
    }
}