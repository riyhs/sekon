package com.sekon.app.network

import com.sekon.app.utils.NetworkInfo
import com.sekon.app.utils.NetworkInfo.BASE_URL
import com.sekon.app.utils.NetworkInfo.TOKEN_KEY
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NetworkConfig {
    fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)
            .addInterceptor(Interceptor(TOKEN_KEY))
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): Api = getRetrofit().create(Api::class.java)
}