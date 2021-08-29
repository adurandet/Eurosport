package com.omnilog.eurosport.home.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.omnilog.eurosport.home.network.*
import com.omnilog.eurosport.home.network.response.GetHomeContentResponse
import com.omnilog.eurosport.mock
import com.omnilog.eurosport.rule.CoroutinesTestRule
import com.omnilog.eurosport.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class HomeRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun io(): CoroutineDispatcher {
            return coroutinesTestRule.testDispatcher
        }
    }

    private val apiInterface: HomeApiInterface = mock()

    private lateinit var repository: HomeRepository

    @Before
    fun setupViewModel() {
        MockitoAnnotations.openMocks(this)
        repository = HomeRepositoryImpl(apiInterface, dispatcherProvider)
    }

    @Test
    fun givenGetHomeContentApiSuccessReturnHomeContentSuccess() {
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

            val response = GetHomeContentResponse(videoApiList, storyApiList)

            Mockito.`when`(apiInterface.getHomeContent()).thenReturn(response)

            repository.homeContentFlow.test {

                repository.getHomeContent()
                Assert.assertEquals(Resource.success(response), expectMostRecentItem())
            }
        }
    }

    @Test
    fun givenGetHomeContentApiExceptionGetHomeContentFlowError() {
        runBlockingTest {
            Mockito.`when`(apiInterface.getHomeContent()).then {
                throw Exception("error")
            }

            repository.homeContentFlow.test {

                repository.getHomeContent()
                Assert.assertEquals(
                    Resource.error<GetHomeContentResponse>("error"),
                    expectMostRecentItem()
                )
            }
        }
    }
}