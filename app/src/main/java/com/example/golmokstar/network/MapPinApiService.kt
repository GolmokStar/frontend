package com.example.golmokstar.network

import com.example.golmokstar.network.dto.MapPinFavoredRequest
import com.example.golmokstar.network.dto.MapPinFavoredResponse
import com.example.golmokstar.network.dto.MapPinRecordRequest
import com.example.golmokstar.network.dto.MapPinVisitRequest
import com.example.golmokstar.network.dto.MapPinVisitResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface MapPinApiService {

    @POST("/mapPin/favored")
    suspend fun favoredPin(@Body mapPinFavoredRequest: MapPinFavoredRequest): Response<MapPinFavoredResponse>

    @POST("/mapPin/visit")
    suspend fun visitPin(@Body mapPinVisitRequest: MapPinVisitRequest): Response<MapPinFavoredResponse>

    @PUT("/mapPin/record")
    suspend fun recordPin(@Body mapPinRecordRequest: MapPinRecordRequest): Response<MapPinVisitResponse>

}