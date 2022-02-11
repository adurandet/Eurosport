package com.eurosport.home.usecase

import com.eurosport.home.model.IHomeItemContent
import com.eurosport.home.model.Story
import com.eurosport.home.model.Video
import com.eurosport.home.network.Resource
import com.eurosport.home.network.Status
import com.eurosport.home.network.response.GetHomeContentResponse
import com.eurosport.home.network.toStory
import com.eurosport.home.network.toVideo
import com.eurosport.home.repository.HomeRepository
import com.eurosport.utils.AndroidDispatcherProvider
import com.eurosport.utils.DispatcherProvider
import com.eurosport.utils.mix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeUseCaseImpl(
    private val repository: HomeRepository,
    private val dispatcherProvider: DispatcherProvider = AndroidDispatcherProvider()
) : HomeUseCase {

    private var scope: CoroutineScope = CoroutineScope(dispatcherProvider.io())

    private val _homeContentFlow = MutableSharedFlow<Resource<List<IHomeItemContent>>>(1)
    override val homeContentFlow: SharedFlow<Resource<List<IHomeItemContent>>> = _homeContentFlow

    init {
        //initFlow()
    }

    private fun initFlow() {
        scope.launch {
            repository.homeContentFlow.collect {
                handleHomeContentFlow(it)
            }
        }
    }

    override suspend fun getHomeContent() {
        withContext(dispatcherProvider.io()) {
            _homeContentFlow.tryEmit(Resource.loading())
            val homeContent = repository.getHomeContent()
            handleHomeContentFlow(homeContent.await())
        }
    }

    private fun handleHomeContentFlow(homeContentResponse: Resource<GetHomeContentResponse>) {
        when (homeContentResponse.status) {
            Status.LOADING -> _homeContentFlow.tryEmit(Resource.loading())
            Status.ERROR -> _homeContentFlow.tryEmit(
                Resource.error(
                    homeContentResponse.message ?: ""
                )
            )
            Status.SUCCESS -> homeContentResponse.data?.let {
                handleHomeContentFlowSuccess(it)
            }
        }
    }

    private fun handleHomeContentFlowSuccess(homeContentResponse: GetHomeContentResponse) {
        val stories: List<Story> = homeContentResponse.stories.map { it.toStory() }.sortedByDescending { it.date }
        val videos: List<Video> = homeContentResponse.videos.map { it.toVideo() }.sortedByDescending { it.date }

        val homeList: List<IHomeItemContent> = stories.mix(videos)
        _homeContentFlow.tryEmit(Resource.success(homeList))
    }
}
