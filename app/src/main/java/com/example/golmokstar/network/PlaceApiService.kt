package com.example.golmokstar.network

import com.example.golmokstar.network.dto.PlaceDetailsResponse
import com.example.golmokstar.network.dto.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    // ✅ 위도·경도로 Place ID 가져오기 (Google Places API - Nearby Search)
    @GET("maps/api/place/nearbysearch/json")  // 🔥 올바른 엔드포인트 설정
    suspend fun getPlaceId(
        @Query("key") apiKey: String,
        @Query("location") location: String,  // ✅ 위도,경도를 하나의 문자열로 전달
        @Query("radius") radius: Int = 50,  // ✅ 기본 반경 설정 (50m)
        @Query("type") type: String = "point_of_interest"  // ✅ 검색 유형 지정 (필수 아님)
    ): PlaceResponse

    // ✅ Place ID로 장소 세부 정보 가져오기
    @GET("maps/api/place/details/json")  // 🔥 올바른 엔드포인트 설정
    suspend fun getPlaceDetails(
        @Query("key") apiKey: String,
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "formatted_address,photo"  // ✅ 필요한 필드만 요청
    ): PlaceDetailsResponse
}
