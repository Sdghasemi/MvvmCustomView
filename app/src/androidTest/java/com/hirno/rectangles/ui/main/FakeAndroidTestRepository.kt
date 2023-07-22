package com.hirno.rectangles.ui.main

import com.hirno.rectangles.data.GenericResponse
import com.hirno.rectangles.data.source.RectanglesRepository
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.network.response.NetworkResponse
import okio.IOException

/**
 * A fake rectangles repository used for instrumented testing
 */
class FakeAndroidTestRepository : RectanglesRepository {

    private val rectangles = ArrayList<RectangleItemModel>()

    var isCacheAvailable = false
    var isNetworkDown = false


    fun addItems(vararg rectangle: RectangleItemModel) {
        rectangles.addAll(rectangle)
    }

    /**
     * Returns suitable answer based on network and cache state
     *
     * @return [NetworkResponse.NetworkError] if there were no cache available while the network is down, [NetworkResponse.Success] otherwise
     */
    override suspend fun getRectangles(): GenericResponse<RectanglesResponseModel> {
        return if (isNetworkDown && !isCacheAvailable) NetworkResponse.NetworkError(IOException())
        else NetworkResponse.Success(RectanglesResponseModel(rectangles))
    }

    /**
     * Updates the passed rectangle in cache
     *
     * @return `true` if the update was successful, `false` otherwise
     */
    override suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean {
        return rectangles.indexOfFirst { it.id == rectangle.id }.takeUnless { it == -1 }?.let { updatedIndex ->
            rectangles[updatedIndex] = rectangle
        } != null
    }
}