package com.omnilog.eurosport.home.repository

import android.util.Log
import com.omnilog.eurosport.home.network.HomeApiInterface
import com.omnilog.eurosport.home.network.Resource
import com.omnilog.eurosport.home.network.response.GetHomeContentResponse
import com.omnilog.eurosport.utils.AndroidDispatcherProvider
import com.omnilog.eurosport.utils.DispatcherProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val homeApiInterface: HomeApiInterface,
    private val dispatcherProvider: DispatcherProvider = AndroidDispatcherProvider()
) : HomeRepository {

    private val _homeContentFlow = MutableSharedFlow<Resource<GetHomeContentResponse>>(1)
    override val homeContentFlow: SharedFlow<Resource<GetHomeContentResponse>> = _homeContentFlow

    override suspend fun getHomeContent() {
        withContext(dispatcherProvider.io()) {
            try {
                _homeContentFlow.tryEmit(Resource.loading())
                val homeContentResponse = homeApiInterface.getHomeContent()
                _homeContentFlow.tryEmit(Resource.success(homeContentResponse))
            } catch (e: Exception) {
                Log.e("HomeRepository", e.stackTraceToString())
                _homeContentFlow.tryEmit(Resource.error(e.message?:""))
            }

        }
    }
}