package com.hirno.rectangles.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirno.rectangles.MainCoroutineRule
import com.hirno.rectangles.R
import com.hirno.rectangles.getOrAwaitValue
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit tests for the implementation of [MainViewModel]
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    // Subject under test
    private lateinit var mainViewModel: MainViewModel

    // Use a fake repository to be injected into the view model
    private lateinit var rectanglesRepository: FakeTestRepository

    // Fake rectangles to be injected into the repository
    private lateinit var fakeRectangles: ArrayList<RectangleItemModel>

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // Add some dummy objects to the repository
        val rectangle1 = RectangleItemModel(
            id = 1,
            x = 0.1f,
            y = 0.3f,
            size = 0.1f,
        )
        val rectangle2 = RectangleItemModel(
            id = 2,
            x = 0.7f,
            y = 0.3f,
            size = 0.3f,
        )
        val rectangle3 = RectangleItemModel(
            id = 3,
            x = 0.5f,
            y = 0.2f,
            size = 0.2f,
        )
        fakeRectangles = arrayListOf(
            rectangle1,
            rectangle2,
            rectangle3,
        )
        // Construct the fake repository
        rectanglesRepository = FakeTestRepository().apply {
            rectangles = fakeRectangles
        }

        mainViewModel = MainViewModel(rectanglesRepository)
    }

    @Test
    fun getRectanglesWithInternetAccess_rectanglesLoadedWithoutError() {
        rectanglesRepository.apply {
            isCacheAvailable = false
            isNetworkDown = false
        }
        mainViewModel.loadRectangles()

        val rectangles = mainViewModel.rectangles.getOrAwaitValue()
        val dataLoading = mainViewModel.dataLoading.getOrAwaitValue()
        val errorText = mainViewModel.errorText.getOrAwaitValue()
        val isDataLoadingError = mainViewModel.isDataLoadingError.getOrAwaitValue()

        assertThat(rectangles, not(nullValue()))
        assertThat(dataLoading, `is`(false))
        assertThat(errorText, `is`(nullValue()))
        assertThat(isDataLoadingError, `is`(false))
    }

    @Test
    fun getRectanglesWhileNetworkIsDown_rectanglesLoadingEncounteredError() {
        rectanglesRepository.apply {
            isCacheAvailable = false
            isNetworkDown = true
        }
        mainViewModel.loadRectangles()

        val dataLoading = mainViewModel.dataLoading.getOrAwaitValue()
        val errorText = mainViewModel.errorText.getOrAwaitValue()
        val isDataLoadingError = mainViewModel.isDataLoadingError.getOrAwaitValue()

        assertThat(dataLoading, `is`(false))
        assertThat(errorText, `is`(R.string.failed_to_connect_to_remote_server))
        assertThat(isDataLoadingError, `is`(true))
    }

    @Test
    fun retryFailedRectanglesRequestAfterReceivingInternetAccess_rectanglesLoadedWithoutError() {
        rectanglesRepository.apply {
            isCacheAvailable = false
            isNetworkDown = true
        }
        mainViewModel.loadRectangles()
        rectanglesRepository.apply {
            isNetworkDown = false
        }
        mainViewModel.onNetworkConnectivityChanged()

        val rectangles = mainViewModel.rectangles.getOrAwaitValue()
        val dataLoading = mainViewModel.dataLoading.getOrAwaitValue()
        val errorText = mainViewModel.errorText.getOrAwaitValue()
        val isDataLoadingError = mainViewModel.isDataLoadingError.getOrAwaitValue()

        assertThat(rectangles, not(nullValue()))
        assertThat(dataLoading, `is`(false))
        assertThat(errorText, `is`(nullValue()))
        assertThat(isDataLoadingError, `is`(false))
    }

    @Test
    fun retryFailedRectanglesRequestWhileNetworkIsStillDown_rectanglesStillFailsToLoad() {
        rectanglesRepository.apply {
            isCacheAvailable = false
            isNetworkDown = true
        }
        mainViewModel.loadRectangles()
        rectanglesRepository.apply {
            isNetworkDown = true
        }
        mainViewModel.onNetworkConnectivityChanged()

        val dataLoading = mainViewModel.dataLoading.getOrAwaitValue()
        val errorText = mainViewModel.errorText.getOrAwaitValue()
        val isDataLoadingError = mainViewModel.isDataLoadingError.getOrAwaitValue()

        assertThat(dataLoading, `is`(false))
        assertThat(errorText, `is`(R.string.failed_to_connect_to_remote_server))
        assertThat(isDataLoadingError, `is`(true))
    }

    @Test
    fun getRectanglesFromCacheWithInternetAccess_rectanglesLoadedWithoutError() {
        rectanglesRepository.apply {
            isCacheAvailable = true
            isNetworkDown = false
        }
        mainViewModel.loadRectangles()

        val rectangles = mainViewModel.rectangles.getOrAwaitValue()
        val dataLoading = mainViewModel.dataLoading.getOrAwaitValue()
        val errorText = mainViewModel.errorText.getOrAwaitValue()
        val isDataLoadingError = mainViewModel.isDataLoadingError.getOrAwaitValue()

        assertThat(rectangles, not(nullValue()))
        assertThat(dataLoading, `is`(false))
        assertThat(errorText, `is`(nullValue()))
        assertThat(isDataLoadingError, `is`(false))
    }

    @Test
    fun getRectanglesFromCacheWhileNetworkIsDown_rectanglesLoadedWithoutError() {
        rectanglesRepository.apply {
            isCacheAvailable = true
            isNetworkDown = true
        }
        mainViewModel.loadRectangles()

        val rectangles = mainViewModel.rectangles.getOrAwaitValue()
        val dataLoading = mainViewModel.dataLoading.getOrAwaitValue()
        val errorText = mainViewModel.errorText.getOrAwaitValue()
        val isDataLoadingError = mainViewModel.isDataLoadingError.getOrAwaitValue()

        assertThat(rectangles, not(nullValue()))
        assertThat(dataLoading, `is`(false))
        assertThat(errorText, `is`(nullValue()))
        assertThat(isDataLoadingError, `is`(false))
    }

    @Test
    fun updateRectangle_rectangleGetsUpdatedIn() {
        rectanglesRepository.apply {
            isCacheAvailable = true
            isNetworkDown = false
        }

        mainViewModel.loadRectangles()

        val updatingRectangle = fakeRectangles[2].copy(
            x = 0.9f,
            y = 0.9f,
        )

        mainViewModel.updateRectangle(updatingRectangle)

        val rectangles = mainViewModel.rectangles.getOrAwaitValue()

        assertThat(rectangles.rectangles[2], `is`(updatingRectangle))
    }
}