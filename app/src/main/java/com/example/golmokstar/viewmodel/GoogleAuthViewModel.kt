package com.example.golmokstar.viewmodel
//
//import android.app.Activity
//import android.app.Application
//import android.util.Log
//import androidx.activity.result.ActivityResult
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.golmokstar.repository.AuthRepository
//import com.example.golmokstar.ui.screens.getGoogleSignInClient
//import com.example.golmokstar.ui.screens.getSignInRequest
//import com.google.android.gms.auth.api.identity.BeginSignInResult
//import com.google.android.gms.auth.api.identity.SignInClient
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class GoogleAuthViewModel(application: Application) : AndroidViewModel(application) {
//    private val signInClient: SignInClient = getGoogleSignInClient(application.applicationContext)
//    private val authRepository = AuthRepository()
//
//    suspend fun signIn(activity: Activity): BeginSignInResult {
//        return signInClient.beginSignIn(getSignInRequest()).await()
//    }
//
//    fun handleSignInResult(result: ActivityResult, onResult: (String) -> Unit) {
//        try {
//            val credential = signInClient.getSignInCredentialFromIntent(result.data)
//            val idToken = credential.googleIdToken
//
//            if (idToken != null) {
//                Log.d("GoogleAuthViewModel", "🔹 Google ID Token: $idToken")
//                viewModelScope.launch {
//                    val success = authRepository.loginWithGoogle(idToken)
//                    if (success != null) {
//                        onResult(idToken)
//                    } else {
//                        onResult("로그인 실패")
//                    }
//                }
//            } else {
//                onResult("로그인 실패")
//            }
//        } catch (e: Exception) {
//            onResult("로그인 실패: ${e.message}")
//        }
//    }
//}
