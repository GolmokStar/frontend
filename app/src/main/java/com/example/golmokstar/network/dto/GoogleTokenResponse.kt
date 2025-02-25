package com.example.golmokstar.network.dto

data class GoogleTokenResponse(
    val googleId: String,
    val code: Int,
    val status: String
)
