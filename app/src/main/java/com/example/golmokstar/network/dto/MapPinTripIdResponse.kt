package com.example.golmokstar.network.dto

data class MapPinTripIdResponse (

    val pinId: Int,
    val placeGoogleId: String,
    val placeName: String,
    val latitude: Double,
    val longitude: Double,
    val pinType: String,  // FAVORED, VISITED, RECORDED 등
    val tripId: Int,
    val tripName: String? = null,  // 기록 상태에서만 있음
    val startDate: String? = null, // 기록 상태에서만 있음
    val endDate: String? = null,   // 기록 상태에서만 있음
    val rating: Double? = null,    // 기록 상태에서만 있음
    val createdAt: String
)

data class ApiMapPinResponse(
    val trips: List<MapPinTripIdResponse> // trips는 리스트입니다.
)