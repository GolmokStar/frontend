package com.example.golmokstar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.golmokstar.network.MapPinApiService
import com.example.golmokstar.network.dto.ApiResponse
import com.example.golmokstar.network.dto.MapPinFavoredRequest
import com.example.golmokstar.network.dto.MapPinRecordRequest
import com.example.golmokstar.network.dto.MapPinResponse
import com.example.golmokstar.network.dto.MapPinVisitRequest
import com.example.golmokstar.network.dto.TripsDropdownResponse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapPinApiService: MapPinApiService  // 생성자 주입
) : ViewModel() {

    // API 호출 결과를 저장할 LiveData
    private val _placeRegisterResult = MutableLiveData<String>()
    val placeRegisterResult: LiveData<String> get() = _placeRegisterResult

    private val _mapPins = MutableLiveData<List<MapPinResponse>>()
    val mapPins: LiveData<List<MapPinResponse>> get() = _mapPins

    private val _dropdownItems = MutableLiveData<List<TripsDropdownResponse>>()
    val dropdownItems: LiveData<List<TripsDropdownResponse>> get() = _dropdownItems

    private val gson = Gson()  // Gson 인스턴스

    // 공통된 API 호출 응답 처리 함수
    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> Response<T>,
        successMessage: String,
        errorMessagePrefix: String
    ) {
        try {

            val response = withContext(Dispatchers.IO) { apiCall() }
            Log.d("API_CALL", "API 요청 시작") // 요청 로그 추가

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
    fun favoredApi(mapPinFavoredRequest: MapPinFavoredRequest) {
        viewModelScope.launch {
            Log.d("API_CALL", "찜하기 요청 데이터: ${gson.toJson(mapPinFavoredRequest)}") // 요청 데이터 로깅
            handleApiResponse(
                apiCall = { mapPinApiService.favoredPin(mapPinFavoredRequest) },
                successMessage = "찜하기 등록",
                errorMessagePrefix = "찜하기 등록"
            )
        }
    }

    // 방문하기 API 호출
    fun visitApi(mapPinVisitRequest: MapPinVisitRequest) {
        viewModelScope.launch {
            Log.d("API_CALL", "방문하기 요청 데이터: ${gson.toJson(mapPinVisitRequest)}") // 요청 데이터 로깅
            handleApiResponse(
                apiCall = { mapPinApiService.visitPin(mapPinVisitRequest) },
                successMessage = "방문하기 요청",
                errorMessagePrefix = "방문하기 요청"
            )
        }
    }

    // 기록하기 API 호출
    fun recordApi(mapPinRecordRequest: MapPinRecordRequest) {
        viewModelScope.launch {
            Log.d("API_CALL", "기록하기 요청 데이터: ${gson.toJson(mapPinRecordRequest)}") // 요청 데이터 로깅
            handleApiResponse(
                apiCall = { mapPinApiService.recordPin(mapPinRecordRequest) },
                successMessage = "기록하기 요청",
                errorMessagePrefix = "기록하기 요청"
            )
        }
    }

    fun mapPinApi() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    mapPinApiService.mapPin()
                }

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("API_CALL", "map API 응답: ${gson.toJson(responseBody)}") // 응답 데이터 로깅

                    _mapPins.postValue(response.body() ?: emptyList())
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류"
                    Log.d("API_CALL", "드롭다운 API 실패: $errorMessage")
                    _placeRegisterResult.postValue("여행 목록 조회 실패: $errorMessage")

                }
            } catch (e: Exception) {
                Log.e("API_CALL", "여행 목록 조회 오류: ${e.message}", e)
                _placeRegisterResult.postValue("네트워크 오류: ${e.message}")
            }
        }
    }

    // 특정 tripId에 해당하는 여행 데이터 호출
    fun mapPinTripIdApi(tripId: Int) {
        viewModelScope.launch {
            try {
                val response = mapPinApiService.mapPintripId(tripId)
                if (response.isSuccessful) {
                    val mapPin = response.body()
                    if (mapPin != null) {
                        _mapPins.postValue(listOf(mapPin)) // 하나의 여행 데이터를 리스트 형태로
                    }
                } else {
                    _placeRegisterResult.postValue("해당 여행 데이터를 가져오지 못했습니다.")
                }
            } catch (e: Exception) {
                _placeRegisterResult.postValue("네트워크 오류: ${e.message}")
            }
        }
    }

    fun dropdownApi() {
        viewModelScope.launch {
            try {
                // API 호출 (Response<ApiResponse> 타입을 명시적으로 지정)
                val response: Response<ApiResponse> = withContext(Dispatchers.IO) {
                    mapPinApiService.dropdownPin() // Retrofit에서 API 호출
                }

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // trips 리스트를 추출하여 _dropdownItems에 저장
                    val trips = responseBody?.trips?.let {
                        val allTrips = it.filter { trip -> trip.title == "전체 여행" } // "전체 여행" 항목 추출
                        val otherTrips = it.filter { trip -> trip.title != "전체 여행" } // 나머지 항목들
                        allTrips + otherTrips // "전체 여행"을 먼저 배치하고 나머지 항목들 이어서 배치
                    } ?: emptyList() // 리스트가 null일 경우 빈 리스트로 설정

                    Log.d("API_CALL", "드롭다운 API 응답: ${gson.toJson(responseBody)}")
                    _dropdownItems.postValue(trips) // trips 리스트를 LiveData에 저장
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류"
                    Log.d("API_CALL", "드롭다운 API 실패: $errorMessage")
                    _placeRegisterResult.postValue("여행 목록 조회 실패: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("API_CALL", "여행 목록 조회 오류: ${e.message}", e)
                _placeRegisterResult.postValue("네트워크 오류: ${e.message}")
            }
        }
    }

}

