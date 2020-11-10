package com.riyaldi.sekonforteacher.network

import com.riyaldi.sekonforteacher.model.saran.SaranResponse
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    // Saran
//    @GET("api/v2/saran/")
//    fun getSaran() : Call<SaranResponse>

//    @POST("api/v2/saran/")
//    fun postSaran(@Body postSaran: PostSaran) : Call<PostSaranResponse>

    @GET("api/v2/saran/")
    fun getSaran() : Call<SaranResponse>

}