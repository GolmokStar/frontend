package com.example.golmokstar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golmokstar.BuildConfig
import com.example.golmokstar.network.PlacesApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesApiService: PlacesApiService
) : ViewModel() {

    private val _placeId = MutableStateFlow<String?>(null)
    val placeId: StateFlow<String?> = _placeId

    private val _placeAddress = MutableStateFlow<String?>(null)
    val placeAddress: StateFlow<String?> = _placeAddress

    private val _placePhotoUrl = MutableStateFlow<String?>(null)
    val placePhotoUrl: StateFlow<String?> = _placePhotoUrl

    private val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY

    /**
     * 1️⃣ 위도·경도로 Place ID 가져오기
     */
    suspend fun fetchPlaceId(latitude: Double, longitude: Double): String? {
        return try {
            val location = "$latitude,$longitude"  // ✅ 위도,경도를 문자열로 변환
            val response = placesApiService.getPlaceId(BuildConfig.GOOGLE_MAPS_API_KEY, location)

            response.places.firstOrNull()?.placeId ?: run {
                Log.e("PlacesViewModel", "❌ Place ID를 찾을 수 없음.")
                null
            }
        } catch (e: Exception) {
            Log.e("PlacesViewModel", "❌ fetchPlaceId 요청 실패: ${e.message}")
            null
        }
    }


    /**
     * 2️⃣ Place ID로 주소 및 사진 가져오기
     */
    suspend fun fetchPlaceDetails(placeId: String): Pair<String?, String?> {
        return try {
            val response = placesApiService.getPlaceDetails(apiKey, placeId)
            val address = response.address
            val photoId = response.photos?.firstOrNull()?.photoId

            val photoUrl = if (photoId != null) {
                "https://places.googleapis.com/v1/places/$photoId/media?maxWidthPx=800&key=$apiKey"
            } else null

            Pair(address, photoUrl)
        } catch (e: Exception) {
            Log.e("PlacesViewModel", "❌ fetchPlaceDetails 요청 실패: ${e.message}")
            Pair(null, null)
        }
    }

    /**
     * 3️⃣ 위도·경도로 Place ID 및 세부 정보 가져오기 (StateFlow 업데이트)
     */
    fun getPlace(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val fetchedPlaceId = fetchPlaceId(latitude, longitude)
                if (fetchedPlaceId != null) {
                    _placeId.value = fetchedPlaceId
                    Log.d("PlacesViewModel", "✅ Place ID: $fetchedPlaceId")

                    val (address, photoUrl) = fetchPlaceDetails(fetchedPlaceId)
                    _placeAddress.value = address
                    _placePhotoUrl.value = photoUrl

                    Log.d("PlacesViewModel", "✅ 주소: $address, 사진 URL: $photoUrl")
                } else {
                    Log.e("PlacesViewModel", "❌ Place ID를 가져오지 못했습니다.")
                }
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "❌ getPlace 요청 실패: ${e.message}")
            }
        }
    }
}
