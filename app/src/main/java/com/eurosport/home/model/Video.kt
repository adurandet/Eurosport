package com.eurosport.home.model

class Video(
    override val id: Long,
    override val title: String,
    override val date: Float,
    val sportName: String,
    val thumb: String,
    val url: String,
    val views: Int
) : IHomeItemContent {
    override val type = HomeContentType.VIDEO
}
