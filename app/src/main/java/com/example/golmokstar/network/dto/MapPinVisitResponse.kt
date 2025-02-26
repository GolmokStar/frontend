package com.example.golmokstar.network.dto

data class MapPinVisitResponse(
    val pinId: String,
    val googlePlaceId : String,
    val latitude : Double,
    val longitude : Double,
    val createdAt : String,
    val message : String
)
