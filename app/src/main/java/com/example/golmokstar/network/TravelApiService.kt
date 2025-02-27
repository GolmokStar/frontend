package com.example.golmokstar.network

import com.example.golmokstar.network.dto.ChangeTravelRequest
import com.example.golmokstar.network.dto.CreateTravelRequest
import com.example.golmokstar.network.dto.CreateTravelResponse
import com.example.golmokstar.network.dto.GetTravelCurrentResponse
import com.example.golmokstar.network.dto.GetTravelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TravelApiService {

    @GET("trips/current")
    suspend fun getTravelCurrent() :  Response<GetTravelCurrentResponse>

    @GET("trips/{tripId}")
    suspend fun getTravel(@Path("tripId") tripsId: String) : Response<GetTravelResponse>

    @POST("trips")
    suspend fun createTravel(@Body request: CreateTravelRequest): Response<CreateTravelResponse>

    @PUT("trips/{tripId}")
    suspend fun changeTravel(@Path("tripId") tripsId: String, @Body request: ChangeTravelRequest) : Response<GetTravelCurrentResponse>
}