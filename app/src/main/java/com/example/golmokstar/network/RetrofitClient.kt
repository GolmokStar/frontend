//package com.example.golmokstar.network
//
//import retrofit2.Retrofit
//import retrofit2.converter.moshi.MoshiConverterFactory
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import com.squareup.moshi.Moshi
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitClient {
//    private const val BASE_URL = "https://1a80-34-64-59-181.ngrok-free.app/"
//
//    private val moshi: Moshi by lazy {
//        Moshi.Builder().build() // Moshi 인스턴스 생성
//    }
//
//    private val client: OkHttpClient by lazy {
//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//    }
//
//    val authService: AuthService by lazy {
//
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create()) // Moshi 컨버터 추가
//            .client(client)
//            .build()
//            .create(AuthService::class.java)
//    }
//}
