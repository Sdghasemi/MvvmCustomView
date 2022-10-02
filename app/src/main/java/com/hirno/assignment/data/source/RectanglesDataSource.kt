package com.hirno.assignment.data.source

import com.hirno.assignment.model.rectangle.RectanglesResponseModel
import com.hirno.assignment.data.GenericResponse
import com.hirno.assignment.model.rectangle.RectangleItemModel

/**
 * Main entry point for accessing rectangles data.
 */
interface RectanglesDataSource {
    suspend fun getRectangles(): GenericResponse<RectanglesResponseModel>

    suspend fun cacheRectangles(rectangles: RectanglesResponseModel): Boolean

    suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean
}