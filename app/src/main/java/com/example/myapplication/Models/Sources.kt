package com.example.myapplication.Models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Sources(
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int,

    @SerializedName("category")
    var category: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("language")
    var language: String,
    @SerializedName("name")
    var name: String,
    var type: String?,
    @SerializedName("url")
    var url: String
)