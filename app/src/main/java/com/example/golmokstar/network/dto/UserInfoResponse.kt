package com.example.golmokstar.network.dto

data class UserInfoResponse(
    val userId: Int?,
    val nickname: String,
    val gender: String,
    val birthDate: String,
    val profilePhoto: String,
    val friendCode: String,
    val recordCount: Int?,
    val interestAreas: List<String>
)

data class FriendsInfoResponse(
    val friendId : Int,
    val profilePhoto: String,
    val nickname: String,
    val friendCode: String,
    val travelCount : String,
    val interests: List<String>
)

data class FriendRequest(
    val requesterFriendCode: String,
    val receiverFriendCode: String
)
