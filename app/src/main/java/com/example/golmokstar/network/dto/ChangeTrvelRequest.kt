package com.example.golmokstar.network.dto

data class ChangeTravelRequest(
    val tripId: String,
    val title: String,
    val startDate: String,
    val endDate: String
)