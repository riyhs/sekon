package com.sekon.app.network

import com.sekon.app.utils.NetworkInfo.BASE_URL
import com.sekon.app.utils.NetworkInfo.TOKEN_FCM
import com.sekon.app.utils.NetworkInfo.TOKEN_KEY
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    private fun getInterceptor(token: String?, authType: String?): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return if (token != null && authType != null) {
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .addInterceptor(Interceptor(token, authType))
                .build()
        } else {
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build()
        }

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getInterceptor(TOKEN_KEY, "bearer"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getCovid(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.covid19api.com/")
            .client(getInterceptor(null, null))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getFCM(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .client(getInterceptor(TOKEN_FCM, "key"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): Api = getRetrofit().create(Api::class.java)
    fun getServiceCovid(): Api = getCovid().create(Api::class.java)
    fun getServiceFCM(): Api = getFCM().create(Api::class.java)
}