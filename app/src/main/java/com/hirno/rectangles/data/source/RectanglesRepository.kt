package com.hirno.rectangles.data.source

import com.hirno.rectangles.data.GenericResponse
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import com.hirno.rectangles.model.rectangle.RectanglesResponseModel

/**
 * Interface to the data layer.
 */
interface RectanglesRepository {
    suspend fun getRectangles(): GenericResponse<RectanglesResponseModel>
    suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean
}