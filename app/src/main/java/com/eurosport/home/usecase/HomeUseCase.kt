package com.eurosport.home.usecase

import com.eurosport.home.model.IHomeItemContent
import com.eurosport.home.network.Resource
import kotlinx.coroutines.flow.SharedFlow

interface HomeUseCase {
    val homeContentFlow: SharedFlow<Resource<List<IHomeItemContent>>>
    suspend fun getHomeContent()
}