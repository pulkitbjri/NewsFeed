package com.example.myapplication.Models


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity
data class SavedNews(

    var type: String?,

    @SerializedName("author")
    var author: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("publishedAt")
    var publishedAt: String?,

    @NotNull
    @PrimaryKey
    @SerializedName("title")
    var title: String,
    @SerializedName("url")
    var url: String?,
    @SerializedName("urlToImage")
    var urlToImage: String?,

    @Embedded(prefix = "source_")
    @SerializedName("source")
    var source: Source
    

):BaseNews()