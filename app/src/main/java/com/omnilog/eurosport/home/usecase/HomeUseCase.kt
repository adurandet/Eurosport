package com.omnilog.eurosport.home.usecase

import com.omnilog.eurosport.home.model.IHomeItemContent
import com.omnilog.eurosport.home.network.Resource
import kotlinx.coroutines.flow.SharedFlow

interface HomeUseCase {
    val homeContentFlow: SharedFlow<Resource<List<IHomeItemContent>>>
    suspend fun getHomeContent()
}