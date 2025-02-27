package com.example.golmokstar.network

import com.example.golmokstar.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    // ✅ OkHttpClient에 AuthInterceptor 추가
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()) // ✅ 인증 Interceptor 적용
            .connectTimeout(30, TimeUnit.SECONDS) // 타임아웃 설정
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)  // ✅ 서버 URL 설정
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient) // ✅ 커스텀 OkHttpClient 적용
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val mapPinFavoredAPI: MapPinApiService by lazy {
        retrofit.create(MapPinApiService::class.java)
    }

    val mapPinVisitAPI: MapPinApiService by lazy {
        retrofit.create(MapPinApiService::class.java)
    }

    val mapPinRecordAPI: MapPinApiService by lazy {
        retrofit.create(MapPinApiService::class.java)
    }

    val mapPinApi: MapPinApiService by lazy {
        retrofit.create(MapPinApiService::class.java)
    }

}
