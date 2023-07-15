package com.practice.cryptocurrency_android.data.dto

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("coinCounter") val coin_counter: Int,
    @SerializedName("icoCounter") val ico_counter: Int,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)