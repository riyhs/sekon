package com.sekon.app.network

import com.sekon.app.model.SiswaResponse
import com.sekon.app.model.SiswaUpdateBody
import com.sekon.app.model.covid.CovidResponse
import com.sekon.app.model.reference.ReferenceResponse
import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.model.signin.SignInResponse
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @POST("api/v2/siswa/signin")
    @Headers(
        "Content-Type: application/json",
        "No-Authentication: true"
    )
    fun postSignIn(@Body dataSignIn: DataSignIn) : Call<SignInResponse>

    @GET("api/v2/siswa/{id}")
    fun getSiswa(@Path( "id") id : String) : Call<SiswaResponse>

    @PUT("api/v2/siswa/{id}")
    fun updateSiswa(
        @Path( "id") id : String,
        @Body siswaUpdateBody: SiswaUpdateBody) : Call<SiswaResponse>

    @GET("api/v2/referensi/{kelas}")
    fun getReferensi(@Path( "kelas") kelas : String) : Call<ReferenceResponse>

    // covid
    @GET("country/indonesia")
    @Headers("No-Authentication: true")
    fun getCovidInfo(@Query("from") from : String, @Query("to") to : String) : Call<CovidResponse>
}