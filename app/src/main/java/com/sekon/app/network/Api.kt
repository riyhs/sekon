package com.sekon.app.network

import com.sekon.app.model.SiswaResponse
import com.sekon.app.model.SiswaUpdateBody
import com.sekon.app.model.absen.PostAbsenResponse
import com.sekon.app.model.absen.PostAbsenBody
import com.sekon.app.model.announcement.AnnouncementPostModel
import com.sekon.app.model.announcement.AnnouncementPostResponse
import com.sekon.app.model.announcement.AnnouncementResponse
import com.sekon.app.model.covid.CovidResponse
import com.sekon.app.model.reference.PostReferenceBody
import com.sekon.app.model.reference.PostReferenceResponse
import com.sekon.app.model.reference.ReferenceResponse
import com.sekon.app.model.saran.PostSaran
import com.sekon.app.model.saran.PostSaranResponse
import com.sekon.app.model.saran.SaranResponse
import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.model.signin.DataSignInTeacher
import com.sekon.app.model.signin.SignInResponse
import com.sekon.app.model.signin.SignInResponseTeacher
import retrofit2.Call
import retrofit2.http.*


interface Api {
    // Sign In
    @POST("api/v2/siswa/signin")
    @Headers(
        "Content-Type: application/json",
        "No-Authentication: true"
    )
    fun postSignIn(@Body dataSignIn: DataSignIn) : Call<SignInResponse>

    @POST("api/v2/guru/signin")
    @Headers(
        "Content-Type: application/json",
        "No-Authentication: true"
    )
    fun postSignInTeacher(@Body dataSignInTeacher: DataSignInTeacher) : Call<SignInResponseTeacher>

    // Siswa
    @GET("api/v2/siswa/{id}")
    fun getSiswa(@Path( "id") id : String) : Call<SiswaResponse>

    @PUT("api/v2/siswa/{id}")
    fun updateSiswa(
        @Path( "id") id : String,
        @Body siswaUpdateBody: SiswaUpdateBody) : Call<SiswaResponse>

    // Referensi
    @GET("api/v2/referensi/{kelas}")
    fun getReferensi(@Path( "kelas") kelas : String) : Call<ReferenceResponse>

    @POST("api/v2/referensi")
    fun postReferensi(@Body postReferenceBody: PostReferenceBody) : Call<PostReferenceResponse>

    // Pengumuman
    @GET("api/v2/pengumuman/")
    fun getAnnouncement() : Call<AnnouncementResponse>

    @POST("api/v2/pengumuman/")
    fun postPengumuman(@Body postPengumuman: AnnouncementPostModel) : Call<AnnouncementPostResponse>

    // Saran
    @GET("api/v2/saran/")
    fun getSaran() : Call<SaranResponse>

    @POST("api/v2/saran/")
    fun postSaran(@Body postSaran: PostSaran) : Call<PostSaranResponse>

    // Absen
    @POST("api/v2/absen/")
    fun postAbsen(@Body postAbsen: PostAbsenBody) : Call<PostAbsenResponse>

    // covid
    @GET("country/indonesia")
    @Headers("No-Authentication: true")
    fun getCovidInfo(@Query("from") from : String, @Query("to") to : String) : Call<CovidResponse>
}