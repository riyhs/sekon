package com.sekon.app.network

import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.model.signin.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {
    @POST("api/v2/siswa/signin")
    @Headers(
        "Content-Type: application/json",
        "No-Authentication: true"
    )
    fun postSignIn(@Body dataSignIn: DataSignIn) : Call<SignInResponse>
}