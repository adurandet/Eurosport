package com.eurosport.home.network.response

import android.os.Parcelable
import com.eurosport.home.network.StoryApiData
import com.eurosport.home.network.VideoApiData
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetHomeContentResponse(
    val videos: List<VideoApiData>,
    val stories: List<StoryApiData>
): Parcelable