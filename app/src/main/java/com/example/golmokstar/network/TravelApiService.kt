package com.example.golmokstar.network

import com.example.golmokstar.network.dto.CreateTravelRequest
import com.example.golmokstar.network.dto.CreateTravelResponse
import com.example.golmokstar.network.dto.GetTravelCurrentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TravelApiService {

    @GET("trips/current")
    suspend fun getTravelCurrent() :  Response<GetTravelCurrentResponse>

    @POST("trips")
    suspend fun createTravel(@Body request: CreateTravelRequest): Response<CreateTravelResponse>
}