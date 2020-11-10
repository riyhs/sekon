package com.sekon.sekonforteacher.network

import com.sekon.sekonforteacher.model.pengumuman.PostPengumuman
import com.sekon.sekonforteacher.model.pengumuman.PostPengumumanResponse
import com.sekon.sekonforteacher.model.saran.SaranResponse
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

    // saran
    @GET("api/v2/saran/")
    fun getSaran() : Call<SaranResponse>

    // pengumuman
    @POST("api/v2/pengumuman/")
    fun postPengumuman(@Body postPengumuman: PostPengumuman) : Call<PostPengumumanResponse>

}