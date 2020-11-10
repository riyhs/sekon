package com.riyaldi.sekonforteacher.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    // Saran
//    @GET("api/v2/saran/")
//    fun getSaran() : Call<SaranResponse>

//    @POST("api/v2/saran/")
//    fun postSaran(@Body postSaran: PostSaran) : Call<PostSaranResponse>

    @GET("api/v2/saran/")
    fun getSaran() : Call

}