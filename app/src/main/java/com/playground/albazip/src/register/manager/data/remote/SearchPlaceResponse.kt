package com.playground.albazip.data.network.response

import com.google.gson.annotations.SerializedName

data class SearchPlaceResponse(
    @SerializedName("documents") val documents: List<Place>  // 검색 결과
)

data class Place(
    @SerializedName("place_name") val place_name: String,               // 장소명, 업체명
    @SerializedName("address_name") val address_name: String,           // 전체 지번 주소
    @SerializedName("road_address_name") val road_address_name: String, // 전체 도로명 주소
    @SerializedName("x") val x: String,                                 // X 좌표값 혹은 longitude
    @SerializedName("y") val y: String                                  // Y 좌표값 혹은 latitude
)