package com.example.golmokstar.network.dto

data class GetTravelResponse(
    val tripId: Int,
    val userId: Int,
    val title: String,
    val startDate: String,
    val endDate: String
)