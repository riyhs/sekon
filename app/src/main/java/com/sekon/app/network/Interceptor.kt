package com.sekon.app.network

import okhttp3.Interceptor
import okhttp3.Response

class Interceptor(private var token: String, private var authType: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("No-Authentication") == null && token.isNotEmpty()) {
            if (authType == "bearer") {
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                    .addHeader("Authorization", finalToken)
                    .build()
            } else if (authType == "key") {
                val finalToken = "key=$token"
                request = request.newBuilder()
                    .addHeader("Authorization", finalToken)
                    .build()
            }

        } else {
            request = request.newBuilder()
                .build()
        }

        return chain.proceed(request)
    }
}