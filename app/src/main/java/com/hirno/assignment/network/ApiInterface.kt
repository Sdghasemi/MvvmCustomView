package com.hirno.assignment.network

import com.hirno.assignment.model.rectangle.RectanglesResponseModel
import com.hirno.assignment.data.GenericResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API interface of the app endpoints
 */
interface ApiInterface {
    @GET("/resourcer/v1/rectangles")
    suspend fun getRectangles(): GenericResponse<RectanglesResponseModel>
}