package com.omnilog.eurosport.home.network

import android.os.Parcelable
import com.omnilog.eurosport.home.model.Story
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class StoryApiData(
    val id: Long,
    val title: String,
    val date: Float,
    val sport: SportApiData,
    val teaser: String,
    val image: String,
    val author: String
): Parcelable

fun StoryApiData.toStory() = Story(
    id,
    title,
    date,
    sport.name,
    teaser,
    image,
    author
)