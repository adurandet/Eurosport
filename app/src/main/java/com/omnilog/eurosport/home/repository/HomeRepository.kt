package com.omnilog.eurosport.home.repository

import com.omnilog.eurosport.home.network.Resource
import com.omnilog.eurosport.home.network.response.GetHomeContentResponse
import kotlinx.coroutines.flow.SharedFlow

interface HomeRepository {
    val homeContentFlow: SharedFlow<Resource<GetHomeContentResponse>>
    suspend fun getHomeContent()
}