package com.hirno.rectangles.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hirno.rectangles.model.rectangle.RectangleItemModel

/**
 * Data Access Object for Rectangles table
 */
@Dao
interface RectanglesDao {
    /**
     * Select all rectangles from the Rectangles table.
     *
     * @return all loaded rectangles.
     */
    @Query("SELECT * FROM Rectangles")
    suspend fun getAll(): List<RectangleItemModel>

    /**
     * Insert rectangles in the database.
     *
     * @param rectangles the rectangles to be inserted.
     */
    @Insert
    suspend fun insertAll(rectangles: List<RectangleItemModel>)

    /**
     * Updates rectangle in the database.
     *
     * @param rectangle the rectangle to update
     * @return the number of updated rows
     */
    @Update
    suspend fun update(rectangle: RectangleItemModel): Int

    /**
     * Delete all rectangles.
     */
    @Query("DELETE FROM Rectangles")
    suspend fun deleteAll()
}