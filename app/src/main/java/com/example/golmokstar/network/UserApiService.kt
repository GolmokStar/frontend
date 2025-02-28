package com.example.golmokstar.network

import com.example.golmokstar.network.dto.ChangeTravelRequest
import com.example.golmokstar.network.dto.CreateTravelRequest
import com.example.golmokstar.network.dto.CreateTravelResponse
import com.example.golmokstar.network.dto.FriendRequest
import com.example.golmokstar.network.dto.FriendsInfoResponse
import com.example.golmokstar.network.dto.GetHistoryResponse
import com.example.golmokstar.network.dto.GetTravelCurrentResponse
import com.example.golmokstar.network.dto.GetTravelResponse
import com.example.golmokstar.network.dto.RecommendResponsed
import com.example.golmokstar.network.dto.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    @GET("users/me")
    suspend fun getUserInformation() : Response<UserInfoResponse>


    @GET("friends/{friendCode}")
    suspend fun getFriendsList(@Path("friendCode") friendCode : String) : Response<Any>

    @GET("friend-requests/{friendCode}")
    suspend fun getRequestsList(@Path("friendCode") friendCode: String) : Response<Any>


    @POST("friend-requests")
    suspend fun createRequest(@Body request: FriendRequest) : Response<Any>

}
