package com.example.golmokstar.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Google 로그인 요청을 보낼 데이터 모델
data class GoogleLoginRequest(val idToken: String)

// 서버에서 받을 응답 데이터 모델
data class LoginResponse(val success: Boolean, val message: String, val code: Int)

interface AuthService {
    @POST("auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): LoginResponse
}
