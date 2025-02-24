package com.example.golmokstar.google

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.golmokstar.network.AuthApiService
import com.example.golmokstar.network.RetrofitClient
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.tasks.await
import com.example.golmokstar.BuildConfig
import com.example.golmokstar.network.dto.GoogleTokenRequest
import com.example.golmokstar.network.dto.GoogleTokenResponse
import com.example.golmokstar.network.dto.SignUpResponse
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class GoogleAuthViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val signInClient: SignInClient = GoogleAuthUtils.getGoogleSignInClient(context)
    private val authService: AuthApiService = RetrofitClient.authApiService
    private val serverClientId: String = BuildConfig.GOOGLE_CLIENT_ID


    // Google Login 요청 실행
    suspend fun signIn(activity: Activity) =
        signInClient.beginSignIn(GoogleAuthUtils.getSignInRequest(serverClientId)).await()

    // 로그인 결과 처리
    fun handleSignInResult(resultData: Intent?): String? {
        return try {
            val credential: SignInCredential =
                signInClient.getSignInCredentialFromIntent(resultData)
            credential.googleIdToken // ID 토큰 반환
        } catch (e: Exception) {
            null // 로그인 실패
        }
    }

//    suspend fun sendTokenToServer(idToken: String): Pair<Int, Any?> {
//        return try {
//            val response = authService.sendGoogleToken(GoogleTokenRequest(idToken))
//
//            val responseBody = response.body() // ✅ JSON이 아니라 객체로 받음
//
//            Log.d("GoogleAuthViewModel", "서버 응답 코드: ${response.code()}")
//            Log.d("GoogleAuthViewModel", "서버 응답 본문: $responseBody")
//
//            response.code() to responseBody
//        } catch (e: Exception) {
//            Log.e("GoogleAuthViewModel", "서버 요청 실패: ${e.message}")
//            500 to null
//        }
//    }



    suspend fun sendTokenToServer(idToken: String): Pair<Int, Any?> {
        return try {
            val response: Response<Any> = authService.sendGoogleToken(GoogleTokenRequest(idToken))

            val responseBodyString = Gson().toJson(response.body()) // ✅ JSON 문자열로 변환
            Log.d("GoogleAuthViewModel", "서버 응답 JSON: $responseBodyString")


            if(response.code() == 200) {
                val signUpResponse = try {
                    Gson().fromJson(responseBodyString, SignUpResponse::class.java)
                } catch (e: JsonSyntaxException) {
                    null
                }
                response.code() to signUpResponse
            } else {
                val googleTokenResponse = try {
                    Gson().fromJson(responseBodyString, GoogleTokenResponse::class.java)
                } catch (e: JsonSyntaxException) {
                    null
                }

                response.code() to googleTokenResponse
            }


        } catch (e: Exception) {
            Log.e("GoogleAuthViewModel", "서버 요청 실패: ${e.message}")
            500 to null
        }
    }


}
