package com.example.golmokstar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.golmokstar.network.TravelApiService
import com.example.golmokstar.network.UserApiService
import com.example.golmokstar.network.dto.FriendRequest
import com.example.golmokstar.network.dto.FriendsInfoResponse
import com.example.golmokstar.network.dto.GetTravelResponse
import com.example.golmokstar.network.dto.UserInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    @Inject
    lateinit var userApiService: UserApiService

    private val _userInfo = MutableStateFlow<UserInfoResponse>(
        UserInfoResponse(
            userId = null,
            nickname = "",
            gender = "",
            birthDate = "",
            profilePhoto = "",
            friendCode = "",
            recordCount = null,
            interestAreas = emptyList()
        )
    )
    val userInfo: StateFlow<UserInfoResponse> = _userInfo


    suspend fun getUserInfo() {
        try {
            val response: Response<UserInfoResponse> = userApiService.getUserInformation()

            Log.e("userInfoResponse", response.toString())

            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    _userInfo.value = UserInfoResponse(
                        userId = userResponse.userId,
                        nickname = userResponse.nickname,
                        gender = userResponse.gender,
                        birthDate = userResponse.birthDate,
                        profilePhoto = userResponse.profilePhoto,
                        friendCode = userResponse.friendCode,
                        recordCount = userResponse.recordCount,
                        interestAreas = userResponse.interestAreas
                    )
                }

                Log.e("userInfo", userInfo.value.toString())

                getUserFriends(userInfo.value.friendCode)
                getUserRequests(userInfo.value.friendCode)

                createRequest(
                    requester = userInfo.value.friendCode,
                    receiver = "4203"
                )
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "userInfo 요청 실패: ${e.message}")
        }

    }


    suspend fun getUserFriends(code : String) {
        try {
            val response: Response<Any> = userApiService.getFriendsList(code)

            Log.e("friendsResponse", response.toString())

            if (response.isSuccessful) {


                Log.e("friends", "ok")
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "friendsList 요청 실패: ${e.message}")
        }
    }


    suspend fun getUserRequests(code: String) {
        try {
            val response: Response<Any> = userApiService.getRequestsList(code)

            Log.e("requestsResponse", response.toString())

            if (response.isSuccessful) {

                Log.e("requests", "ok")
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "requestsList 요청 실패: ${e.message}")
        }
    }

    suspend fun createRequest(requester: String, receiver: String) {
        try {
            val response: Response<Any> = userApiService.createRequest(FriendRequest(
                requesterFriendCode = requester,
                receiverFriendCode = receiver
            ))

            Log.e("request", response.toString())

            if (response.isSuccessful) {

                Log.e("request", "ok")
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "friend 요청 실패: ${e.message}")
        }
    }

}

