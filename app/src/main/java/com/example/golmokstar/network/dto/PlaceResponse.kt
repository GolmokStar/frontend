package com.example.golmokstar.network.dto


import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("places") val places: List<Place>
)

data class Place(
    @SerializedName("id") val placeId: String
)

data class PlaceDetailsResponse(
    @SerializedName("formattedAddress") val address: String,
    @SerializedName("photos") val photos: List<Photo>?
)

data class Photo(
    @SerializedName("name") val photoId: String
)
