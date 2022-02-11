package com.eurosport.home.repository

import com.eurosport.home.network.Resource
import com.eurosport.home.network.response.GetHomeContentResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.SharedFlow

interface HomeRepository {
    val homeContentFlow: SharedFlow<Resource<GetHomeContentResponse>>
    suspend fun getHomeContent(): Deferred<Resource<GetHomeContentResponse>>
}