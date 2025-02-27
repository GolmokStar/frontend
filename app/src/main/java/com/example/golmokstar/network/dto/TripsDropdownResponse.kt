package com.example.golmokstar.network.dto

data class TripsDropdownResponse(
    val tripId: Int,
    val title: String
)

data class ApiResponse(
    val trips: List<TripsDropdownResponse> // trips는 리스트입니다.
)

