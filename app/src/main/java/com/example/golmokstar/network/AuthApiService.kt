package com.example.golmokstar.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/google")
    suspend fun sendGoogleToken(@Body token: String): Response<Unit>


}
