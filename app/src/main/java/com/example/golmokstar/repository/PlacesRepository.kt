package com.example.golmokstar.repository

import com.example.golmokstar.BuildConfig
import com.example.golmokstar.network.PlacesApiService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    @Named("PlacesRetrofit") private val placesApiService: PlacesApiService // ✅ Google Places API Retrofit 사용
) {
    private val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY

//    suspend fun fetchPlaceId(latitude: Double, longitude: Double): String? {
//        //val response = placesApiService.getPlaceId(apiKey, latitude, longitude)
//       //return response.places.firstOrNull()?.placeId
//    }

    suspend fun fetchPlaceDetails(placeId: String): Pair<String?, String?> {
        val response = placesApiService.getPlaceDetails(apiKey, placeId)
        val address = response.address
        val photoId = response.photos?.firstOrNull()?.photoId

        val photoUrl = if (photoId != null) {
            "https://places.googleapis.com/v1/places/$photoId/media?maxWidthPx=800&key=$apiKey"
        } else null

        return Pair(address, photoUrl)
    }
}
