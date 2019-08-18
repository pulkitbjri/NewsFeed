package com.example.myapplication.Models

import androidx.lifecycle.MutableLiveData
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MainNewsModel(

    var title: String,
    var type: String,
    var newsList: MutableLiveData<ArrayList<BaseNews>>


)