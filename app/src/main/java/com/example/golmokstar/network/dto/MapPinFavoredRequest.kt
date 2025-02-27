package com.example.golmokstar.network.dto

data class MapPinFavoredRequest(
    val tripId: Int,
    val googlePlaceId: String,
    val placeName: String,
    val placeType: String,
    val latitude : Double,
    val longitude : Double,
    val pinType: String
)

