package com.example.golmokstar.repository
//
//import android.util.Log
//import com.example.golmokstar.network.GoogleLoginRequest
//import com.example.golmokstar.network.LoginResponse
//import com.example.golmokstar.network.RetrofitClient
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//
//class AuthRepository {
//    suspend fun loginWithGoogle(idToken: String): LoginResponse? {
//        return withContext(Dispatchers.IO) { // 네트워크 호출을 IO 스레드에서 실행
//            try {
//                val response = RetrofitClient.authService
//                    .loginWithGoogle(GoogleLoginRequest(idToken)) // suspend fun 호출
//
//                Log.d("AuthRepository", "서버 응답: ${response}")
//                response
//            } catch (e: HttpException) {
//                Log.e("AuthRepository", "HTTP 오류: ${e.response()?.errorBody()?.string()}")
//                null
//            } catch (e: Exception) {
//                Log.e("AuthRepository", "네트워크 오류: ${e.message}")
//                null
//            }
//        }
//    }
//}
