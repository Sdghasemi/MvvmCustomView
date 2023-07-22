package com.hirno.rectangles.data.source.local

import com.hirno.rectangles.data.source.RectanglesDataSource
import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.data.GenericResponse
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import com.hirno.rectangles.network.response.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Rectangles local data source implementation
 *
 * @property rectanglesDao The DAO reference used to perform database related actions
 * @property ioDispatcher The coroutines dispatcher used to perform DAO operations on
 */
class RectanglesLocalDataSource(
    private val rectanglesDao: RectanglesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RectanglesDataSource {
    /**
     * Get all rectangles from local storage
     *
     * @return A [NetworkResponse.Success] when all rectangles are retrieved successfully
     * or a [NetworkResponse.UnknownError] with the exception that occurred
     */
    override suspend fun getRectangles(): GenericResponse<RectanglesResponseModel> = withContext(ioDispatcher) {
        return@withContext try {
            NetworkResponse.Success(RectanglesResponseModel(ArrayList(rectanglesDao.getAll())))
        } catch (e: Exception) {
            NetworkResponse.UnknownError(e)
        }
    }

    /**
     * Deletes all existing rectangles from local storage and inserts the new ones
     *
     * @param rectangles The new rectangles
     * @return `true` if the insertion was successful, `false` otherwise
     */
    override suspend fun cacheRectangles(rectangles: RectanglesResponseModel): Boolean {
        return try {
            rectanglesDao.deleteAll()
            rectanglesDao.insertAll(rectangles.rectangles)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Updates rectangle properties based on its id
     *
     * @param rectangle The updated rectangle
     * @return `true` if the update was successful, `false` otherwise
     */
    override suspend fun updateRectangle(rectangle: RectangleItemModel): Boolean {
        return try {
            return rectanglesDao.update(rectangle) > 0
        } catch (e: Exception) {
            false
        }
    }
}