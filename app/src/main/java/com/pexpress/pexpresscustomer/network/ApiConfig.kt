package com.pexpress.pexpresscustomer.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        //  url
//        const val URL = "http://192.168.16.227:8000" // server local
        const val URL = "https://pex.pexpress.my.id"

        const val URL_IMG_BANNER = "$URL/filebanner/"
        const val URL_LOGO_VA = "$URL/filebank/"
        const val URL_LOGO_EWALLET = "$URL/fileewallet/"
        private const val ENDPOINT = "$URL/api/"
        private const val ENDPOINTDIRECTIONS = "https://maps.googleapis.com/maps/api/"

        private fun client(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        fun getApiService(isGetDirections: Boolean = false): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(if (isGetDirections) ENDPOINTDIRECTIONS else ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}