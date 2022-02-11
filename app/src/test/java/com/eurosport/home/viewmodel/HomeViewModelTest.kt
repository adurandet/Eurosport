package com.eurosport.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import com.eurosport.forClass
import com.eurosport.home.HomeAction
import com.eurosport.home.HomeViewModel
import com.eurosport.home.HomeViewState
import com.eurosport.home.model.IHomeItemContent
import com.eurosport.home.model.Story
import com.eurosport.home.model.Video
import com.eurosport.home.network.Resource
import com.eurosport.home.usecase.HomeUseCase
import com.eurosport.mock
import com.eurosport.rule.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val observer: Observer<HomeViewState> = mock()
    private val useCase: HomeUseCase = mock()

    private val sharedFlow = MutableSharedFlow<Resource<List<IHomeItemContent>>>(1)

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        MockitoAnnotations.openMocks(this)
        `when`(useCase.homeContentFlow).thenReturn(sharedFlow)

        homeViewModel = HomeViewModel(useCase)
        homeViewModel.viewState.observeForever(observer)
    }

    @After
    fun tearDown() {
        homeViewModel.viewState.removeObserver(observer)
    }

    @Test
    fun givenGetHomeContentGetLoadingState() {
        runBlockingTest {
            homeViewModel.getHomeContent()

            val captor: ArgumentCaptor<HomeViewState> = forClass()
            captor.run {
                verify(observer).onChanged(capture())
                assertEquals(HomeViewState.Loading, value)
            }
        }
    }

    @Test
    fun givenGetHomeContentUseCaseSuccessGetViewStateSuccess() {
        val contentList = listOf(
            Story(
                123L,
                "Title",
                1630136737.000f,
                "Soccer",
                "teaser",
                "http://url.com/image.jpeg",
                "author"
            ),
            Video(
                123L,
                "Title",
                1630136737.000f,
                "Soccer",
                "http://url.com/thumb.jpeg",
                "http://url.com/video.mp4",
                1250
            )
        )

        runBlockingTest {
            `when`(useCase.getHomeContent()).then {
                sharedFlow.tryEmit(Resource.loading())
                sharedFlow.tryEmit(Resource.success(contentList))
            }

            homeViewModel.getHomeContent()

            val captor: ArgumentCaptor<HomeViewState> = forClass()
            captor.run {
                verify(observer, Mockito.times(3)).onChanged(capture())
                assertEquals(
                    HomeViewState.Success(contentList),
                    value
                )
            }
        }
    }

    @Test
    fun givenGetHomeContentUseCaseErrorGetViewStateError() {
        runBlockingTest {
            val errorMessage = "error"

            `when`(useCase.getHomeContent()).then {
                sharedFlow.tryEmit(Resource.loading())
                sharedFlow.tryEmit(Resource.error(errorMessage))
            }

            homeViewModel.getHomeContent()

            val captor: ArgumentCaptor<HomeViewState> = forClass()
            captor.run {
                verify(observer, Mockito.times(3)).onChanged(capture())
                assertEquals(
                    HomeViewState.Error(errorMessage),
                    value
                )
            }
        }
    }

    @Test
    fun givenItemVideoClickedGetGoToVideoPlayerAction() {
        runBlockingTest {
            homeViewModel.actions.test {
                val url = "http://url.com/video.mp4"
                homeViewModel.onVideoClickListener(url)

                assertEquals(
                    HomeAction.GoToVideoPlayer(url),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun givenItemStoryClickedGetGoToStoryDetailAction() {
        runBlockingTest {
            homeViewModel.actions.test {
                val story = Story(
                    123L,
                    "Title",
                    1630136737.000f,
                    "Soccer",
                    "teaser",
                    "http://url.com/image.jpeg",
                    "author"
                )
                homeViewModel.onStoryClickListener(story)

                assertEquals(
                    HomeAction.GoToStoryDetail(story),
                    awaitItem()
                )
            }
        }
    }
}