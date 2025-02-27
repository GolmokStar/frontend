package com.example.golmokstar.viewmodel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.golmokstar.R
import com.example.golmokstar.network.HistoryApiService
import com.example.golmokstar.network.TravelApiService
import com.example.golmokstar.network.dto.ChangeTravelRequest
import com.example.golmokstar.network.dto.GetTravelCurrentResponse
import com.example.golmokstar.network.dto.GetTravelResponse
import com.example.golmokstar.network.dto.CreateTravelRequest
import com.example.golmokstar.network.dto.CreateTravelResponse
import com.example.golmokstar.network.dto.GetHistoryResponse
import com.example.golmokstar.network.dto.RecommendResponsed
import com.example.golmokstar.network.dto.TripsDropdownResponse
import com.example.golmokstar.utils.formatToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

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

data class PlaceIdResponse(
    val results: List<PlaceResult>
)

data class PlaceResult(
    val place_id: String
)

data class PlacePhotoResponse(
    val photoUrl: String
)




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


    private val _currentError = MutableStateFlow(Error.NONE)
    val currentError: StateFlow<Error> = _currentError


    private val _recentHistoryList = MutableStateFlow<List<GetHistoryResponse>>(emptyList())
    val recentHistoryList: StateFlow<List<GetHistoryResponse>> = _recentHistoryList

    private val _historyList = MutableStateFlow<List<GetHistoryResponse>>(emptyList())
    val historyList: StateFlow<List<GetHistoryResponse>> = _historyList

    private val _currentTripList = MutableStateFlow<TripsDropdownResponse>(TripsDropdownResponse(0, "선택해주세요"))
    val currentTripList: StateFlow<TripsDropdownResponse> = _currentTripList

    private val _aiPlaceList = MutableStateFlow<List<RecommendResponsed>>(emptyList())
    val aiPlaceList: StateFlow<List<RecommendResponsed>> = _aiPlaceList

    // Geocoder 객체
    private val geocoder = Geocoder(application.applicationContext, Locale.getDefault())



    fun updateSelectedTrip(newItem: TripsDropdownResponse) {
        _currentTripList.value = newItem
        getHistory(newItem.tripId.toString()) // 선택한 여행에 따라 히스토리 갱신
        Log.d("currentTripList", currentTripList.value.toString())
    }

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


    fun getAIPlace() {
        viewModelScope.launch {
            try {
                val response = travelApiService.getRecommend()
                if (response.isSuccessful) {
                    Log.e("AIresponse", response.body().toString())
                    response.body()?.let { placeResponse ->
                        _aiPlaceList.value =  placeResponse  // ✅ 리스트를 직접 저장
                    } ?: run {
                        _aiPlaceList.value = emptyList()
                    }
                } else {
                    Log.e("서버 응답 오류","${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("네트워크 요청 실패", " ${e.message}")
            }
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

    fun getHistory(tripId : String) {
        viewModelScope.launch {
            try {
                val response = travelApiService.getHistory(tripId)
                if (response.isSuccessful) {
                    response.body()?.let { historyResponse ->
                        _historyList.value = historyResponse  // ✅ 리스트를 직접 저장
                    } ?: run {
                        _historyList.value = emptyList()
                    }
                } else {
                    Log.e("서버 응답 오류","${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("네트워크 요청 실패", " ${e.message}")
            }
        }
    }

    fun getPlaceImageByCoordinates(latitude: Double, longitude: Double, onResult: (String?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 위도, 경도 정보로 주소 변환
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0)

                Log.d("TravelViewModel", "주소 변환 결과: $address")  // 주소가 제대로 반환되는지 확인

                // 주소가 존재하면 placeId 가져오기
                address?.let {
                    val placeId = getPlaceIdFromAddress(it)
                    Log.d("TravelViewModel", "placeId: $placeId")  // placeId가 제대로 반환되는지 확인
                    placeId?.let { id ->
                        // placeId로 이미지 URL 가져오기
                        val imageUrl = getPlacePhotoByPlaceId(id)
                        Log.d("TravelViewModel", "이미지 URL: $imageUrl")  // 이미지 URL 확인
                        withContext(Dispatchers.Main) {
                            onResult(imageUrl, id)  // 이미지 URL과 장소 ID 전달
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            onResult(null, null)  // placeId를 찾지 못한 경우
                        }
                    }
                } ?: run {
                    withContext(Dispatchers.Main) {
                        onResult(null, null)  // 주소를 찾지 못한 경우
                    }
                }
            } catch (e: Exception) {
                Log.e("TravelViewModel", "위도 경도로 주소 변환 및 이미지 가져오기 실패: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(null, null)  // 실패 시 null 전달
                }
            }
        }
    }

    private fun getPlaceIdFromAddress(address: String): String? {
        val apiKey = getApplication<Application>().getString(R.string.google_API_key) // API 키 가져오기
        val urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$apiKey"

        Log.d("TravelViewModel", "Geocode API 호출 URL: $urlString")  // API 호출 URL 확인

        try {
            // URL 객체 생성
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            // 응답을 읽기
            val inputStream = urlConnection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            // JSON 응답 파싱
            val jsonResponse = JSONObject(response.toString())
            val results = jsonResponse.getJSONArray("results")

            Log.d("TravelViewModel", "Geocode API 응답: $response")  // 응답 내용 확인

            // 첫 번째 결과에서 placeId 추출
            if (results.length() > 0) {
                val result = results.getJSONObject(0)
                return result.getString("place_id")
            }
        } catch (e: Exception) {
            Log.e("TravelViewModel", "주소로 PlaceId 가져오기 실패: ${e.message}")
        }
        return null
    }

    private fun getPlacePhotoByPlaceId(placeId: String): String? {
        val apiKey = getApplication<Application>().getString(R.string.google_API_key) // API 키 가져오기
        val urlString = "https://maps.googleapis.com/maps/api/place/details/json" +
                "?place_id=$placeId" +
                "&language=ko" +
                "&key=$apiKey"

        try {
            // URL 객체 생성
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            // 응답을 읽기
            val inputStream = urlConnection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            // JSON 응답 파싱
            val jsonResponse = JSONObject(response.toString())
            val result = jsonResponse.getJSONObject("result")

            // "photos" 배열이 있는지 확인
            val photos = result.optJSONArray("photos")

            if (photos != null && photos.length() > 0) {
                val photoReference = photos.getJSONObject(0).getString("photo_reference")
                // 사진 URL 생성 (최대 너비 400으로 설정)
                return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=$apiKey"
            } else {
                Log.e("TravelViewModel", "사진 정보가 없습니다.")
            }
        } catch (e: Exception) {
            Log.e("TravelViewModel", "장소 세부정보 및 사진 가져오기 실패: ${e.message}")
        }
        return null
    }
}