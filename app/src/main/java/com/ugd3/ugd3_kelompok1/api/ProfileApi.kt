package com.ugd3.ugd3_kelompok1.api

class ProfileApi {
    companion object {
        val BASE_URL = "http://10.113.98.213/UGD_Kelompok1/public/api/"

        val GET_ALL_URL = BASE_URL + "profileUser"
        val GET_BY_ID_URL = BASE_URL + "profileUser/"
        val ADD_URL = BASE_URL + "profileUser"
        val UPDATE_URL = BASE_URL + "profileUser/"
        val DELETE_URL = BASE_URL + "profileUser/"
    }
}