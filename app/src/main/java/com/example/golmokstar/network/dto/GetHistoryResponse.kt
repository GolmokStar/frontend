package com.example.golmokstar.network.dto

data class GetHistoryResponse(
    val recordId : Int,
    val pinId : Int,
    val tripTitle : String,
    val placeName : String,
    val googlePlaceId : String,
    val comment : String,
    val photo : String
)