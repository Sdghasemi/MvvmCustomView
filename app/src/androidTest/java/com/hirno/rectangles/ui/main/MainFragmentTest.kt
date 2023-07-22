package com.hirno.rectangles.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.hirno.rectangles.R
import com.hirno.rectangles.ServiceLocator
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class MainFragmentTest {

    // Use a fake repository to be injected into the [ServiceLocator]
    private lateinit var repository: FakeAndroidTestRepository

    // The rectangle used for testing update procedure
    private lateinit var swipingRectangle: RectangleItemModel

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository().apply {
            // Add some dummy objects to the repository
            val rectangle1 = RectangleItemModel(
                id = 1,
                x = 0.1f,
                y = 0.8f,
                size = 0.1f,
            )
            val rectangle2 = RectangleItemModel(
                id = 2,
                x = 0.7f,
                y = 0.6f,
                size = 0.3f,
            )
            val rectangle3 = RectangleItemModel(
                id = 3,
                x = 0.5f,
                y = 0.2f,
                size = 0.5f,
            ).also { swipingRectangle = it }
            addItems(rectangle1, rectangle2, rectangle3)
        }
        ServiceLocator.rectanglesRepository = repository
    }

    @After
    fun cleanupDb() = runTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun openFragmentWithInternetAccess_RectanglesAreLoadedIntoView() = runTest {
        repository.apply {
            isCacheAvailable = false
            isNetworkDown = false
        }

        launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_App)

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        onView(withId(R.id.message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rectangles)).check(matches(isDisplayed()))
    }

    @Test
    fun openFragmentWhileNetworkIsDown_OnlyErrorMessageIsDisplayed() = runTest {
        repository.apply {
            isCacheAvailable = false
            isNetworkDown = true
        }

        launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_App)

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        onView(withId(R.id.message)).check(matches(withText(R.string.failed_to_connect_to_remote_server)))
        onView(withId(R.id.rectangles)).check(matches(not(isDisplayed())))
    }

    @Test
    fun openFragmentWhileNetworkIsDownWithCache_RectanglesAreLoadedFromCacheIntoView() = runTest {
        repository.apply {
            isCacheAvailable = true
            isNetworkDown = true
        }

        launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_App)

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        onView(withId(R.id.message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rectangles)).check(matches(isDisplayed()))
    }

    @Test
    fun moveARectangle_RectangleMovesAndGetsUpdatedInCache() = runTest {
        repository.apply {
            isCacheAvailable = true
            isNetworkDown = false
        }

        launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_App)

        onView(withId(R.id.rectangles)).perform(swipeDown())

        val isMoved = swipingRectangle.y != .2f
        assertThat("Swiped rectangle did not update!", isMoved, `is`(true))
    }
}