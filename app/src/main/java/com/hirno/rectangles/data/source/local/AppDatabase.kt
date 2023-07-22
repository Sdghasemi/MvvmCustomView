package com.hirno.rectangles.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hirno.rectangles.model.rectangle.RectangleItemModel

/**
 * The Room Database that contains the Rectangles table.
 */
@Database(entities = [RectangleItemModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rectanglesDao(): RectanglesDao
}