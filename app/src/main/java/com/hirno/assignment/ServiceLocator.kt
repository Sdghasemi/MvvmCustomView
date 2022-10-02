package com.hirno.assignment

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.hirno.assignment.data.source.RectanglesDataSource
import com.hirno.assignment.data.source.RectanglesRepository
import com.hirno.assignment.data.source.DefaultRectanglesRepository
import com.hirno.assignment.data.source.local.AppDatabase
import com.hirno.assignment.data.source.local.RectanglesLocalDataSource
import com.hirno.assignment.data.source.remote.RectanglesRemoteDataSource

/**
 * A Service Locator for the [RectanglesRepository]. This is the prod version, with a
 * the "real" [RectanglesRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    private var database: AppDatabase? = null
    @Volatile
    var rectanglesRepository: RectanglesRepository? = null
        @VisibleForTesting set

    /**
     * Returns the prod version of [RectanglesRepository]
     *
     * @param context The application context
     * @return The prod version of [RectanglesRepository]
     */
    fun provideRectanglesRepository(context: Context): RectanglesRepository {
        synchronized(this) {
            return rectanglesRepository ?: createRectanglesRepository(context)
        }
    }

    private fun createRectanglesRepository(context: Context): RectanglesRepository {
        val newRepo = DefaultRectanglesRepository(context, RectanglesRemoteDataSource, createRectanglesLocalDataSource(context))
        rectanglesRepository = newRepo
        return newRepo
    }

    private fun createRectanglesLocalDataSource(context: Context): RectanglesDataSource {
        val database = database ?: createDatabase(context)
        return RectanglesLocalDataSource(database.rectanglesDao())
    }

    private fun createDatabase(context: Context): AppDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "App.db"
        ).build()
        database = result
        return result
    }

    /**
     * Resets repository data sources. Used for integration testing
     */
    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            rectanglesRepository = null
        }
    }
}