package com.example.golmokstar.network

import com.example.golmokstar.network.dto.GoogleTokenRequest
import com.example.golmokstar.network.dto.GoogleTokenResponse
import com.example.golmokstar.network.dto.SignUpRequest
import com.example.golmokstar.network.dto.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/google")
    suspend fun sendGoogleToken(@Body request:GoogleTokenRequest): Response<Any>


    @POST("auth/google/signup")
    suspend fun signUp(@Body request: SignUpRequest) : Response<SignUpResponse>
}
