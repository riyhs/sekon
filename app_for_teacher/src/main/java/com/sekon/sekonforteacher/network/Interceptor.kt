package com.sekon.sekonforteacher.network

import okhttp3.Interceptor
import okhttp3.Response

class Interceptor(private val token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = if (request.header("No-Authentication") == null && token.isNotEmpty()) {
            val finalToken = "Bearer $token"
            request.newBuilder()
                .addHeader("Authorization", finalToken)
                .build()
        } else {
            request.newBuilder()
                .build()
        }

        return chain.proceed(request)
    }
}