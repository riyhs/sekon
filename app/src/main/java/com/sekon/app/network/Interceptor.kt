package com.sekon.app.network

import okhttp3.Interceptor
import okhttp3.Response

class Interceptor(private var token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val finalToken = "Bearer $token"
        val req = chain.request()
        val basicReq = req.newBuilder()
            .addHeader("Authorization", finalToken)
            .build()

        return chain.proceed(basicReq)
    }
}