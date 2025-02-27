package com.example.golmokstar.network.dto

data class CreateTravelRequest(
    val userId: Int,
    val title: String,
    val startDate: String,
    val endDate: String
)
