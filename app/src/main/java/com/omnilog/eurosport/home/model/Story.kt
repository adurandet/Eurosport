package com.omnilog.eurosport.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    override val id: Long,
    override val title: String,
    override val date: Float,
    val sportName: String,
    val teaser: String,
    val image: String,
    val author: String
): IHomeItemContent, Parcelable{
    override val type = HomeContentType.STORY
}
