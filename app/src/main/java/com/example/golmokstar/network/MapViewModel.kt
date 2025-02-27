package com.example.golmokstar.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golmokstar.network.RetrofitClient.mapPinApi
import com.example.golmokstar.network.RetrofitClient.mapPinFavoredAPI
import com.example.golmokstar.network.RetrofitClient.mapPinRecordAPI
import com.example.golmokstar.network.RetrofitClient.mapPinVisitAPI
import com.example.golmokstar.network.dto.MapPinFavoredRequest
import com.example.golmokstar.network.dto.MapPinRecordRequest
import com.example.golmokstar.network.dto.MapPinResponse
import com.example.golmokstar.network.dto.MapPinVisitRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MapViewModel : ViewModel() {

    // API 호출 결과를 저장할 LiveData
    private val _placeRegisterResult = MutableLiveData<String>()
    val placeRegisterResult: LiveData<String> get() = _placeRegisterResult

    private val _mapPins = MutableLiveData<List<MapPinResponse>>()  // 장소 리스트 저장
    val mapPins: LiveData<List<MapPinResponse>> get() = _mapPins  // 외부에서 관찰 가능


    private val gson = Gson()  // Gson 인스턴스

    // 공통된 API 호출 응답 처리 함수
    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> Response<T>,
        successMessage: String,
        errorMessagePrefix: String
    ) {
        try {
            val response = withContext(Dispatchers.IO) { apiCall() }

            val responseJson = gson.toJson(response.body()) // 응답 객체를 Gson으로 변환
            Log.d("API_CALL", "서버 응답 코드: ${response.code()}")
            Log.d("API_CALL", "서버 응답 본문: $responseJson")

            if (response.isSuccessful) {
                Log.d("API_CALL", "$successMessage 성공!")
                _placeRegisterResult.postValue("$successMessage 성공!")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류"
                _placeRegisterResult.postValue("$errorMessagePrefix 실패: $errorMessage")
                Log.d("API_CALL", "$errorMessagePrefix 실패: $errorMessage")
            }
        } catch (e: Exception) {
            // 예외 처리 (네트워크 문제 등)
            Log.e("API_CALL", "네트워크 오류: ${e.message}", e)
            _placeRegisterResult.postValue("네트워크 오류: ${e.message}")
        }
    }

    // 장소 찜하기 API 호출
    fun favoredPin(mapPinFavoredRequest: MapPinFavoredRequest) {
        viewModelScope.launch {
            handleApiResponse(
                apiCall = { mapPinFavoredAPI.favoredPin(mapPinFavoredRequest) },
                successMessage = "찜하기 등록",
                errorMessagePrefix = "찜하기 등록"
            )
        }
    }

    // 방문하기 API 호출
    fun visitPin(mapPinVisitRequest: MapPinVisitRequest) {
        viewModelScope.launch {
            handleApiResponse(
                apiCall = { mapPinVisitAPI.visitPin(mapPinVisitRequest) },
                successMessage = "방문하기 요청",
                errorMessagePrefix = "방문하기 요청"
            )
        }
    }

    // 기록하기 API 호출
    fun recordPin(mapPinRecordRequest: MapPinRecordRequest) {
        viewModelScope.launch {
            handleApiResponse(
                apiCall = { mapPinRecordAPI.recordPin(mapPinRecordRequest) },
                successMessage = "기록하기 요청",
                errorMessagePrefix = "기록하기 요청"
            )
        }
    }

    // 장소 조회(전체) API 호출
    fun mapPin() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { mapPinApi.mapPin() }

                if (response.isSuccessful) {
                    val mapPinList = response.body() ?: emptyList()  // null인 경우 빈 리스트로 대체
                    _mapPins.postValue(mapPinList)  // 장소 목록을 LiveData에 저장
                    Log.d("API_CALL", "장소 조회 성공!")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류"
                    Log.d("API_CALL", "장소 조회 실패: $errorMessage")
                }
            } catch (e: Exception) {
                // 예외 처리
                Log.e("API_CALL", "장소 조회 네트워크 오류: ${e.message}", e)
            }
        }
    }

}
