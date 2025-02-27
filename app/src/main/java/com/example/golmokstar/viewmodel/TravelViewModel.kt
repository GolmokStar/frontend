package com.example.golmokstar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.golmokstar.network.HistoryApiService
import com.example.golmokstar.network.TravelApiService
import com.example.golmokstar.network.dto.ChangeTravelRequest
import com.example.golmokstar.network.dto.GetTravelCurrentResponse
import com.example.golmokstar.network.dto.GetTravelResponse
import com.example.golmokstar.network.dto.CreateTravelRequest
import com.example.golmokstar.network.dto.CreateTravelResponse
import com.example.golmokstar.network.dto.GetHistoryResponse
import com.example.golmokstar.utils.formatToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch

enum class TravelState {
    NONE, TRAVELING, SETTING
}

data class Travel(
    val id: String = "",
    val title: String = "",
    val startDate: String = "",
    val endDate: String = ""
)

enum class Error {
    NONE, // 에러 없음
    Title, // 제목 없음
    Date, // 날짜 없음
    Both, // 둘 다 없음
}



@HiltViewModel
class TravelViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    @Inject
    lateinit var travelApiService: TravelApiService

    private val _travelState = MutableStateFlow<TravelState>(TravelState.NONE)
    val travelState: StateFlow<TravelState> = _travelState

    private val _currentTravel = MutableStateFlow<Travel?>(null)
    val currentTravel: StateFlow<Travel?> = _currentTravel

    private val _historyList = MutableStateFlow<List<GetHistoryResponse>>(emptyList())  // ✅ 리스트로 변경
    val historyList: StateFlow<List<GetHistoryResponse>> = _historyList  // ✅ 리스트로 변경


    private val _currentError = MutableStateFlow(Error.NONE)
    val currentError: StateFlow<Error> = _currentError


    private val _recentHistoryList = MutableStateFlow<List<GetHistoryResponse>>(emptyList())
    val recentHistoryList: StateFlow<List<GetHistoryResponse>> = _recentHistoryList



    suspend fun getCurrentTravel() {
        try {
            val response: Response<GetTravelCurrentResponse> = travelApiService.getTravelCurrent()
            Log.d("TravelViewModel", "서버 응답: ${response.body()}")

            if (response.isSuccessful) {
                response.body()?.let { travelResponse ->
                    if(travelResponse.tripId == null) {
                        _travelState.value = TravelState.NONE
                    } else {
                        _travelState.value = TravelState.TRAVELING
                        getTravel(travelResponse.tripId.toString())
                    }
                }
            } else {
                _travelState.value = TravelState.NONE
            }
        } catch (e: Exception) {
            Log.e("TravelViewModel", "서버 요청 실패: ${e.message}")
            _travelState.value = TravelState.NONE
        }
    }



    suspend fun getTravel(tripId: String) {
        try {
            val response: Response<GetTravelResponse> = travelApiService.getTravel(tripId)
            if (response.isSuccessful) {
                response.body()?.let { travelResponse ->
                    _currentTravel.value = Travel(
                        id = travelResponse.tripId.toString(),
                        title = travelResponse.title,
                        startDate = travelResponse.startDate,
                        endDate = travelResponse.endDate
                    )
                }

                Log.e("travel", currentTravel.value.toString())
            }
        } catch (e: Exception) {
            Log.e("TravelViewModel", "getTravel 요청 실패: ${e.message}")
        }
    }

    fun validateAndSetTravelState() {
        when (_travelState.value) {
            TravelState.TRAVELING -> {
                Log.e("state", _travelState.value.toString())
                _travelState.value = TravelState.SETTING
            }
            TravelState.NONE -> {
                Log.e("state", _travelState.value.toString())
                _travelState.value = TravelState.SETTING
            }
            else -> {
                Log.e("state", _travelState.value.toString())
            }
        }
    }

    fun setTravel(travelPlan : Travel) {
        when {
            travelPlan.title.isEmpty() && (travelPlan.startDate.isEmpty() || travelPlan.endDate.isEmpty()) -> {
                _currentError.value = Error.Both
            }
            travelPlan.title.isEmpty() -> {
                _currentError.value = Error.Title
            }
            travelPlan.startDate.isEmpty() || travelPlan.endDate.isEmpty() -> {
                _currentError.value = Error.Date
            }
            else -> {
                _currentError.value = Error.NONE

                if(_currentTravel.value == null) {
                    Log.d("travel", "여행 등록")
                    viewModelScope.launch {
                        createTravel(
                            CreateTravelRequest(
                                travelPlan.title,
                                travelPlan.startDate.formatToDate(),
                                travelPlan.endDate.formatToDate()
                            )
                        )
                    }
                }
                else if (
                    _currentTravel.value?.title == travelPlan.title &&
                    _currentTravel.value?.startDate == travelPlan.startDate &&
                    _currentTravel.value?.endDate == travelPlan.endDate
                ) {
                    Log.d("travel", "바뀐거없음")
                    _travelState.value = TravelState.TRAVELING
                }
                else {
                    Log.d("travel", "여행 재설정")
                    _currentTravel.value?.id?.let { tripId ->
                        viewModelScope.launch {
                            changeTravel(tripId, ChangeTravelRequest(tripId, travelPlan.title, travelPlan.startDate.formatToDate(), travelPlan.endDate.formatToDate()))
                        }
                    }
                }
            }
        }
    }

    suspend fun changeTravel(tripId: String, request: ChangeTravelRequest) {
        try {
            Log.e("request",  request.toString())
            val response: Response<GetTravelCurrentResponse> = travelApiService.changeTravel(tripId, request)
            Log.d("TravelViewModel", "changeTravel 응답: ${response.body()}")

            if (response.isSuccessful) {
                response.body()?.let { travelResponse ->
                    _travelState.value = TravelState.TRAVELING
                    getTravel(travelResponse.tripId.toString())
                }
            } else {
                _travelState.value = TravelState.NONE
            }
        } catch (e: Exception) {
            Log.e("TravelViewModel", "changeTravel 요청 실패: ${e.message}")
        }
    }

    suspend fun createTravel(request: CreateTravelRequest) {
        Log.e("request",  request.toString())
        try {
            val response: Response<CreateTravelResponse> = travelApiService.createTravel(request)
            Log.d("TravelViewModel", "createTravel 응답: ${response.body()}")

            if (response.isSuccessful) {
                response.body()?.let { travelResponse ->
                    _travelState.value = TravelState.TRAVELING
                    getTravel(travelResponse.tripId.toString())
                }
            } else {
                Log.e("TravelViewModel", "서버 응답 실패: ${response.code()} - ${response.message()}")
                _travelState.value = TravelState.NONE
            }
        } catch (e: Exception) {
            Log.e("TravelViewModel", "서버 요청 실패: ${e.message}")
        }
    }

    fun getRecentHistory() {
        viewModelScope.launch {
            try {
                val response = travelApiService.getRecentHistory()
                if (response.isSuccessful) {
                    response.body()?.let { historyResponse ->
                        _recentHistoryList.value = historyResponse  // ✅ 리스트를 직접 저장
                    } ?: run {
                        _recentHistoryList.value = emptyList()
                    }
                } else {
                    Log.e("서버 응답 오류","${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("네트워크 요청 실패", " ${e.message}")
            }
        }
    }
}
