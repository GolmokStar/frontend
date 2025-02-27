package com.example.golmokstar.network.dto

data class CreateTravelResponse(
    val success: Boolean,
    val tripId: Int,
    val message: String
)