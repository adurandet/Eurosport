package com.omnilog.eurosport.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnilog.eurosport.home.model.IHomeItemContent
import com.omnilog.eurosport.home.model.Story
import com.omnilog.eurosport.home.network.Resource
import com.omnilog.eurosport.home.network.Status
import com.omnilog.eurosport.home.usecase.HomeUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: HomeUseCase
): ViewModel(), OnVideoClickListener, OnStoryClickListener {

    private val _viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewState: LiveData<HomeViewState> = _viewState

    private val _actionsSharedFlow = MutableSharedFlow<HomeAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val actions: Flow<HomeAction> = _actionsSharedFlow.asSharedFlow()

    init {
        initFlow()
    }

    private fun initFlow() {
        viewModelScope.launch {
            useCase.homeContentFlow.collect {
               handleHomeContentFlow(it)
            }
        }
    }

    private fun handleHomeContentFlow(homeContentList: Resource<List<IHomeItemContent>>) {
       when(homeContentList.status){
           Status.LOADING -> _viewState.value = HomeViewState.Loading
           Status.ERROR -> _viewState.value = HomeViewState.Error(homeContentList.message?:"")
           Status.SUCCESS -> homeContentList.data?.let {
               _viewState.value = HomeViewState.Success(it)
           }
       }
    }

    fun getHomeContent() {
        viewModelScope.launch {
            _viewState.value = HomeViewState.Loading
            useCase.getHomeContent()
        }
    }

    override fun onVideoClickListener(url: String) {
        _actionsSharedFlow.tryEmit(HomeAction.GoToVideoPlayer(url))
    }

    override fun onStoryClickListener(story: Story) {
        _actionsSharedFlow.tryEmit(HomeAction.GoToStoryDetail(story))
    }
}