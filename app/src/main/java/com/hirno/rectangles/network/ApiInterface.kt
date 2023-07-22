package com.hirno.rectangles.network

import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.data.GenericResponse
import retrofit2.http.GET

/**
 * API interface of the app endpoints
 */
interface ApiInterface {
    @GET("/resourcer/v1/rectangles")
    suspend fun getRectangles(): GenericResponse<RectanglesResponseModel>
}