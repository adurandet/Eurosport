package com.eurosport.home.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SportApiData(
    val name: String
): Parcelable