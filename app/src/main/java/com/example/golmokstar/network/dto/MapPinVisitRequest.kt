package com.example.golmokstar.network.dto

data class MapPinVisitRequest(
    val tripId: String,
    val googlePlaceId: String,
    val placeName: String,
    val latitude : Double,
    val longitude : Double,
    val deviceLatitude : Double,
    val deviceLongitude : Double,
    val pinType: String
)
