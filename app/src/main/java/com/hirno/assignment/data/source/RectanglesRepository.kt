package com.hirno.assignment.data.source

import com.hirno.assignment.data.GenericResponse
import com.hirno.assignment.model.rectangle.RectangleItemModel
import com.hirno.assignment.model.rectangle.RectanglesResponseModel

/**
 * Interface to the data layer.
 */
interface RectanglesRepository {
    suspend fun getRectangles(): GenericResponse<RectanglesResponseModel>
    suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean
}