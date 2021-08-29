package com.omnilog.eurosport.home.network

import android.os.Parcelable
import com.omnilog.eurosport.home.model.Video
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoApiData(
    val id: Long,
    val title: String,
    val date: Float,
    val sport: SportApiData,
    val thumb: String,
    val url: String,
    val views: Int
) : Parcelable

fun VideoApiData.toVideo() = Video(
    id,
    title,
    date,
    sport.name,
    thumb,
    url,
    views
)