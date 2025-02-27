package com.example.golmokstar.network.dto

data class MapPinFavoredResponse(
    val pinId: String,
    val googlePlaceId : String,
    val latitude : Double,
    val longitude : Double,
    val createdAt : String,
    val remainingDays : Int,
    val message : String
)