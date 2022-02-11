package com.eurosport.home.repository

import android.util.Log
import com.eurosport.home.network.HomeApiInterface
import com.eurosport.home.network.Resource
import com.eurosport.home.network.response.GetHomeContentResponse
import com.eurosport.utils.AndroidDispatcherProvider
import com.eurosport.utils.DispatcherProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class HomeRepositoryImpl(
    private val homeApiInterface: HomeApiInterface,
    private val dispatcherProvider: DispatcherProvider = AndroidDispatcherProvider()
) : HomeRepository {

    private val _homeContentFlow = MutableSharedFlow<Resource<GetHomeContentResponse>>(1)
    override val homeContentFlow: SharedFlow<Resource<GetHomeContentResponse>> = _homeContentFlow

    override suspend fun getHomeContent(): Deferred<Resource<GetHomeContentResponse>>{
        return GlobalScope.async {
//            try {
//                _homeContentFlow.tryEmit(Resource.loading())
                val homeContentResponse = homeApiInterface.getHomeContent()
                Resource.success(homeContentResponse)
//                _homeContentFlow.tryEmit(Resource.success(homeContentResponse))
//            } catch (e: Exception) {
//                Log.e("HomeRepository", e.stackTraceToString())
//                _homeContentFlow.tryEmit(Resource.error(e.message?:""))
//            }

        }
    }
}