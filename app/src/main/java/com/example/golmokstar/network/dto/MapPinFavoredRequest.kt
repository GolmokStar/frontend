package com.example.golmokstar.network.dto

data class MapPinFavoredRequest(
    val tripId: String,
    val googlePlaceId: String,
    val placeName: String,
    val latitude : Double,
    val longitude : Double,
    val pinType: String
)

