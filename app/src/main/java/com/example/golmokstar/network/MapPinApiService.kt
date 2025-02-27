package com.example.golmokstar.network

import com.example.golmokstar.network.dto.ApiResponse
import com.example.golmokstar.network.dto.MapPinFavoredRequest
import com.example.golmokstar.network.dto.MapPinFavoredResponse
import com.example.golmokstar.network.dto.MapPinRecordRequest
import com.example.golmokstar.network.dto.MapPinResponse
import com.example.golmokstar.network.dto.MapPinVisitRequest
import com.example.golmokstar.network.dto.MapPinVisitResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MapPinApiService {

    @POST("/mapPin/favored")
    suspend fun favoredPin(@Body mapPinFavoredRequest: MapPinFavoredRequest): Response<MapPinFavoredResponse>

    @POST("/mapPin/visit")
    suspend fun visitPin(@Body mapPinVisitRequest: MapPinVisitRequest): Response<MapPinFavoredResponse>

    @PUT("/mapPin/record")
    suspend fun recordPin(@Body mapPinRecordRequest: MapPinRecordRequest): Response<MapPinVisitResponse>

    @GET("/mapPin")
    suspend fun mapPin(): Response<List<MapPinResponse>>

    // 특정 여행 아이디를 이용하여 여행 데이터 조회
    @GET("/mapPin/{tripId}")
    suspend fun mapPintripId(@Path("tripId") tripId: Int): Response<MapPinResponse>


    @GET("/trips/dropdown")
    suspend fun dropdownPin(): Response<ApiResponse>

}