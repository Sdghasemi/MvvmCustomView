package com.hirno.rectangles.data.source

import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.data.GenericResponse
import com.hirno.rectangles.model.rectangle.RectangleItemModel

/**
 * Main entry point for accessing rectangles data.
 */
interface RectanglesDataSource {
    suspend fun getRectangles(): GenericResponse<RectanglesResponseModel>

    suspend fun cacheRectangles(rectangles: RectanglesResponseModel): Boolean

    suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean
}