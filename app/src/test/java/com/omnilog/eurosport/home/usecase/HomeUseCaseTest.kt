package com.omnilog.eurosport.home.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.omnilog.eurosport.home.model.HomeContentType
import com.omnilog.eurosport.home.model.IHomeItemContent
import com.omnilog.eurosport.home.network.*
import com.omnilog.eurosport.home.network.response.GetHomeContentResponse
import com.omnilog.eurosport.home.repository.HomeRepository
import com.omnilog.eurosport.mock
import com.omnilog.eurosport.rule.CoroutinesTestRule
import com.omnilog.eurosport.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class HomeUseCaseTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun io(): CoroutineDispatcher {
            return coroutinesTestRule.testDispatcher
        }
    }

    private val repository: HomeRepository = mock()

    private val sharedFlow = MutableSharedFlow<Resource<GetHomeContentResponse>>(1)

    private lateinit var useCase: HomeUseCase

    @Before
    fun setupViewModel() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(repository.homeContentFlow).thenReturn(sharedFlow)
        useCase = HomeUseCaseImpl(repository, dispatcherProvider)
    }

    @Test
    fun givenGetHomeContentRepositorySuccessGetHomeContentFlowSuccessWithOrderedContent() {
        runBlockingTest {
            val storyApiList = listOf(
                StoryApiData(
                    1,
                    "Title",
                    1630000123.000f,
                    SportApiData("Soccer"),
                    "teaser",
                    "http://url.com/image.jpeg",
                    "author"
                ),
                StoryApiData(
                    2,
                    "Title",
                    1630000456.000f,
                    SportApiData("Soccer"),
                    "teaser",
                    "http://url.com/image.jpeg",
                    "author"
                )
            )

            val videoApiList = listOf(
                VideoApiData(
                    3L,
                    "Title",
                    1630000123.000f,
                    SportApiData("Soccer"),
                    "http://url.com/thumb.jpeg",
                    "http://url.com/video.mp4",
                    1250
                ),
                VideoApiData(
                    4L,
                    "Title",
                    1630000456.000f,
                    SportApiData("Soccer"),
                    "http://url.com/thumb.jpeg",
                    "http://url.com/video.mp4",
                    1250
                )
            )

            Mockito.`when`(repository.getHomeContent()).then {
                sharedFlow.tryEmit(
                    Resource.success(
                        GetHomeContentResponse(
                            videoApiList,
                            storyApiList
                        )
                    )
                )
            }

            useCase.homeContentFlow.test {

                useCase.getHomeContent()

                val expectedHomeContentOrderedIdList = listOf(
                    2L,
                    4L,
                    1L,
                    3L,
                )

                val successResource = expectMostRecentItem()
                assertEquals(Status.SUCCESS, successResource.status)

                val listOfIds = successResource.data?.map { it.id }
                assertEquals(expectedHomeContentOrderedIdList, listOfIds)

                assertEquals(HomeContentType.STORY, successResource.data?.get(0)?.type)
                assertEquals(HomeContentType.VIDEO, successResource.data?.get(1)?.type)
                assertEquals(HomeContentType.STORY, successResource.data?.get(2)?.type)
                assertEquals(HomeContentType.VIDEO, successResource.data?.get(3)?.type)
            }
        }
    }

    @Test
    fun givenGetHomeContentRepositoryErrorGetHomeContentFlowError() {
        runBlockingTest {
            Mockito.`when`(repository.getHomeContent()).then {
                sharedFlow.tryEmit(Resource.error(""))
            }

            useCase.homeContentFlow.test {

                useCase.getHomeContent()

                assertEquals(
                    Resource.error<List<IHomeItemContent>>(""),
                    expectMostRecentItem()
                )
            }
        }
    }
}