package com.example.myapplication.Models


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class SavedNews(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var type: String?,

    @SerializedName("author")
    var author: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("publishedAt")
    var publishedAt: String?,

    @SerializedName("title")
    var title: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("urlToImage")
    var urlToImage: String?,

    @Embedded(prefix = "source_")
    @SerializedName("source")
    var source: Source
    

):BaseNews()