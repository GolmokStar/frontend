package com.example.golmokstar.google

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.golmokstar.network.AuthApiService
import com.example.golmokstar.network.RetrofitClient
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.tasks.await
import com.example.golmokstar.BuildConfig
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


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
            val credential: SignInCredential = signInClient.getSignInCredentialFromIntent(resultData)
            credential.googleIdToken // ID 토큰 반환
        } catch (e: Exception) {
            null // 로그인 실패
        }
    }

    // 서버에 Google ID 토큰 전송
    suspend fun sendTokenToServer(idToken: String): Int {
        return try {
            authService.sendGoogleToken(idToken).code() // 서버 응답 코드 반환
        } catch (e: Exception) {
            500 // 서버 오류
        }
    }
}
