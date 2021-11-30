package com.playground.albazip.src.register.manager.data.remote

import com.google.gson.annotations.SerializedName

data class PostMSignUpRequest(
    @SerializedName("name") val name : String,
    @SerializedName("type") val type : String,
    @SerializedName("address") val address : String,
    @SerializedName("ownerName") val ownerName : String,
    @SerializedName("registerNumber") val registerNumber : String,
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String,
    @SerializedName("holiday") val holiday : ArrayList<String>,
    @SerializedName("payday") val payday : String
)