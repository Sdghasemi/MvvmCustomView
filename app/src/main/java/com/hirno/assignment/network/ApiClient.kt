package com.hirno.assignment.network

import com.hirno.assignment.BuildConfig
import com.hirno.assignment.network.response.NetworkResponseAdapterFactory
import okhttp3.*
import okhttp3.internal.immutableListOf
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object used to access the Retrofit instance
 */
object ApiClient {
    private const val SERVER_BASE_URL = "https://example.com/"

    val retrofit: ApiInterface by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .protocols(immutableListOf(Protocol.HTTP_1_1))
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiInterface::class.java)
    }
}