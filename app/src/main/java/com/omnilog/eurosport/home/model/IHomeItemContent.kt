package com.omnilog.eurosport.home.model

interface IHomeItemContent {
    val id: Long
    val title: String
    val date: Float
    val type: HomeContentType
}