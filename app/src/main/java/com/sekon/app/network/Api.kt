package com.sekon.app.network

import com.sekon.app.model.test.SiswaGetResponse
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("api/v1/siswa/")
    fun getSiswa() : Call<SiswaGetResponse>
}