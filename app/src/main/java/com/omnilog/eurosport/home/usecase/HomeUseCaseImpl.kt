package com.omnilog.eurosport.home.usecase

import com.omnilog.eurosport.home.model.IHomeItemContent
import com.omnilog.eurosport.home.model.Story
import com.omnilog.eurosport.home.model.Video
import com.omnilog.eurosport.home.network.Resource
import com.omnilog.eurosport.home.network.Status
import com.omnilog.eurosport.home.network.response.GetHomeContentResponse
import com.omnilog.eurosport.home.network.toStory
import com.omnilog.eurosport.home.network.toVideo
import com.omnilog.eurosport.home.repository.HomeRepository
import com.omnilog.eurosport.utils.AndroidDispatcherProvider
import com.omnilog.eurosport.utils.DispatcherProvider
import com.omnilog.eurosport.utils.mix
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
        initFlow()
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
            repository.getHomeContent()
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
