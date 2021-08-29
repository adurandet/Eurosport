package com.omnilog.eurosport.home

import com.omnilog.eurosport.home.model.IHomeItemContent
import com.omnilog.eurosport.home.model.Story

sealed class HomeViewState {
    object Loading: HomeViewState()
    data class Error(val message: String): HomeViewState()
    data class Success(val contentList: List<IHomeItemContent>): HomeViewState()
}

sealed class HomeAction {
    data class GoToVideoPlayer(val url: String): HomeAction()
    data class GoToStoryDetail(val story: Story): HomeAction()
}
