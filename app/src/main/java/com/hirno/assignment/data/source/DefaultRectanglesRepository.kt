package com.hirno.assignment.data.source

import android.content.Context
import com.hirno.assignment.data.GenericResponse
import com.hirno.assignment.model.rectangle.RectangleItemModel
import com.hirno.assignment.model.rectangle.RectanglesResponseModel
import com.hirno.assignment.network.response.NetworkResponse
import kotlinx.coroutines.delay

/**
 * Concrete implementation to load rectangles from the data sources into a cache.
 */
class DefaultRectanglesRepository(
    private val applicationContext: Context,
    private val rectanglesRemoteDataSource: RectanglesDataSource,
    private val rectanglesLocalDataSource: RectanglesDataSource,
) : RectanglesRepository {
    companion object {
        /**
         * Max cache lifetime in milliseconds
         */
        const val MAX_CACHE_LIFETIME_MILLIS = 7 * 24 * 60 * 60 * 1000
    }

    /**
     * Checks rectangles cache before retrieval. If the cache was still valid, will use local storage to load rectangles.
     * Otherwise it will load them from server
     */
    override suspend fun getRectangles(): GenericResponse<RectanglesResponseModel> {
        val preferences = applicationContext.getSharedPreferences("cache_expiry", Context.MODE_PRIVATE)
        val rectanglesCacheExpiry = preferences.getLong("rectangles", 0)
        val isCacheExpired = rectanglesCacheExpiry < System.currentTimeMillis()
        return if (isCacheExpired) getRectanglesFromRemoteDataSource()
        else getRectanglesFromLocalDataSource()
    }

    /**
     * Gets rectangles from remote data source and updates the cache
     */
    private suspend fun getRectanglesFromRemoteDataSource(): GenericResponse<RectanglesResponseModel> {
        return rectanglesRemoteDataSource.getRectangles().also { result ->
            if (result is NetworkResponse.Success) {
                rectanglesLocalDataSource.cacheRectangles(result.body).let { cached ->
                    if (cached) {
                        updateCacheExpiry()
                    }
                }
            }
        }
    }

    /**
     * Gets rectangles from local data source and updates the cache expiry
     */
    private suspend fun getRectanglesFromLocalDataSource(): GenericResponse<RectanglesResponseModel> {
        return rectanglesLocalDataSource.getRectangles().also { result ->
            if (result is NetworkResponse.Success) {
                updateCacheExpiry()
            }
        }
    }

    /**
     * Updates cache expiry
     */
    private fun updateCacheExpiry() {
        applicationContext.getSharedPreferences("cache_expiry", Context.MODE_PRIVATE).edit().apply {
            putLong("rectangles", System.currentTimeMillis() + MAX_CACHE_LIFETIME_MILLIS)
        }.apply()
    }

    /**
     * Updates the modified rectangle in local data source
     */
    override suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean {
        return rectanglesLocalDataSource.updateRectangle(rectangle)
    }
}