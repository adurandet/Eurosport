package com.omnilog.eurosport.home.network.response

import android.os.Parcelable
import com.omnilog.eurosport.home.network.StoryApiData
import com.omnilog.eurosport.home.network.VideoApiData
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetHomeContentResponse(
    val videos: List<VideoApiData>,
    val stories: List<StoryApiData>
): Parcelable