package com.example.golmokstar.storage

object TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    fun saveTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun getAccessToken(): String? {
        return accessToken
    }

    fun getRefreshToken(): String? {
        return refreshToken
    }
}
