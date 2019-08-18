package com.example.myapplication.Models

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MainNewsModel(

    var title: String,
    var type: String,
    var newsList: MutableLiveData<ArrayList<BaseNews>> ?,
    var sourceList: MutableLiveData<ArrayList<Sources>>?


) : Serializable