package com.example.golmokstar.network

import android.util.Log
import com.example.golmokstar.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenManager.getAccessToken()

        val newRequest = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                header("Authorization", "Bearer $token")
            }
        }.build()

        // ✅ 헤더 로그 출력
        Log.d("AuthInterceptor", "Request URL: ${newRequest.url}")
        Log.d("AuthInterceptor", "Authorization Header: ${newRequest.headers["Authorization"]}")

        return chain.proceed(newRequest)
    }
}
