package com.hirno.rectangles.data.source.remote

import com.hirno.rectangles.data.source.RectanglesDataSource
import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.data.GenericResponse
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import com.hirno.rectangles.network.response.NetworkResponse

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
//        return ApiClient.retrofit.getRectangles()
        return NetworkResponse.Success(RectanglesResponseModel(
            rectangles = arrayListOf(
                RectangleItemModel(
                    id = 1,
                    x = 0.3f,
                    y = 0.5f,
                    size = 0.2f
                ),
                RectangleItemModel(
                    id = 2,
                    x = 0.7f,
                    y = 0.7f,
                    size = 0.4f
                ),
                RectangleItemModel(
                    id = 3,
                    x = 0.6f,
                    y = 0.2f,
                    size = 0.3f
                ),
            )
        ))
    }

    override suspend fun cacheRectangles(rectangles: RectanglesResponseModel): Boolean {
        throw UnsupportedOperationException("Cannot insert anything on remote data source!")
    }

    override suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean {
        throw UnsupportedOperationException("Cannot update anything on remote data source!")
    }
}