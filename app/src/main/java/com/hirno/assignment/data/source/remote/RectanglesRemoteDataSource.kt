package com.hirno.assignment.data.source.remote

import com.hirno.assignment.data.source.RectanglesDataSource
import com.hirno.assignment.model.rectangle.RectanglesResponseModel
import com.hirno.assignment.data.GenericResponse
import com.hirno.assignment.model.rectangle.RectangleItemModel
import com.hirno.assignment.network.ApiClient
import com.hirno.assignment.network.response.NetworkResponse
import kotlinx.coroutines.delay

/**
 * Rectangles remote data source implementation
 */
object RectanglesRemoteDataSource : RectanglesDataSource {
    /**
     * Retrieves rectangles from server
     *
     * @return Server response of the network call
     */
    override suspend fun getRectangles(): GenericResponse<RectanglesResponseModel> {
        return ApiClient.retrofit.getRectangles()
    }

    override suspend fun cacheRectangles(rectangles: RectanglesResponseModel): Boolean {
        throw UnsupportedOperationException("Cannot insert anything on remote data source!")
    }

    override suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean {
        throw UnsupportedOperationException("Cannot update anything on remote data source!")
    }
}