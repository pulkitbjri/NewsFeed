package com.example.myapplication.Models


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    var source_id: String?,
    @SerializedName("name")
    var name: String?
)