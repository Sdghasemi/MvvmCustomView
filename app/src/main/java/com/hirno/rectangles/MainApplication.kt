package com.hirno.rectangles

import android.app.Application
import com.hirno.rectangles.data.source.RectanglesRepository

class MainApplication : Application() {
    /**
     * Rectangles repository used for accessing rectangles inside the app itself
     */
    val rectanglesRepository: RectanglesRepository
        get() = ServiceLocator.provideRectanglesRepository(this)
}