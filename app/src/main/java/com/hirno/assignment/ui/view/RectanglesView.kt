package com.hirno.assignment.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.SystemClock
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.appcompat.content.res.AppCompatResources
import com.hirno.assignment.R
import com.hirno.assignment.model.rectangle.RectangleItemModel
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * A custom view designed to draw rectangles based on their properties in
 * [RectangleItemModel]. Each property is resolved against the view dimensions.
 */
class RectanglesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
): View(context, attrs) {
    companion object {
        private const val CLICK_SLOP_THRESHOLD = 64
        private val CLICK_TIMEOUT = ViewConfiguration.getTapTimeout()
    }

    /**
     * Rectangles array list to draw on view
     */
    var rectangles = ArrayList<RectangleItemModel>()
        set(value) {
            field = value
            postInvalidate()
        }

    /**
     * Callback for any updates caused by moving any rectangle
     */
    var updateListener: UpdateListener? = null

    /**
     * The rectangle drawable to draw on canvas
     */
    private val rectDrawable = AppCompatResources.getDrawable(
        context,
        R.drawable.rectangle
    )!!

    /**
     * Holds the starting milliseconds of the first touch (ACTION_DOWN)
     */
    private var clickStartMillis = 0L
    /**
     * Holds the first touch coordinates (ACTION_DOWN)
     */
    private var clickStartPoint = PointF()

    /**
     * Holds the touched rectangle reference
     */
    private var touchedRectangle: RectangleItemModel? = null
        set(value) {
            field = value
            originalTouchedRectangle = value?.copy()
        }
    /**
     * Holds a copy of the touched rectangle before any modifications
     */
    private var originalTouchedRectangle: RectangleItemModel? = null

    /**
     * Stores the coordinates where user is moving their finger
     */
    private var touchedPoint = PointF()

    /**
     * Returns the touched rectangle based on user's first touch coordinates
     */
    private fun getClickedRectangle(): RectangleItemModel? {
        val hitTestRect = RectF()
        return rectangles.firstOrNull { rectangle ->
            hitTestRect.apply {
                set(
                    rectangle.leftBound,
                    rectangle.topBound,
                    rectangle.rightBound,
                    rectangle.bottomBound,
                )
            }.let { rect ->
                rect contains clickStartPoint
            }
        }
    }

    /**
     * Processes touches based on the event action. Moves the rectangle or performs simple click
     *
     * @param event The touch event
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Mark the first touch coordinates and time
                clickStartPoint.set(event.x, event.y)
                clickStartMillis = SystemClock.uptimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                // Acquire a reference to the touched rectangle if found any
                if (touchedRectangle == null) {
                    getClickedRectangle()?.let { clickedRect ->
                        touchedRectangle = clickedRect
                        onRectangleMovementStarted()
                    } ?: return false
                }
                // Update the rectangle's center based on the touch coordinates
                touchedRectangle?.apply {
                    touchedPoint.set(event.x, event.y)
                    val diffXInPx = touchedPoint.x - clickStartPoint.x
                    val diffYInPx = touchedPoint.y - clickStartPoint.y
                    val diffX = diffXInPx / width
                    val diffY = diffYInPx / height
                    val originalRect = originalTouchedRectangle!!
                    val newCenterX = originalRect.x + diffX
                    val newCenterY = originalRect.y + diffY
                    x = newCenterX
                    y = newCenterY
                }?.also {
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                // Check whether the movement falls within the usual click gesture
                // to call [performClick] otherwise consider finishing the movement
                val movedDistance = sqrt(
                    abs(clickStartPoint.x - event.x).toDouble().pow(2.0)
                            + abs(clickStartPoint.y - event.y).toDouble().pow(2.0)
                )
                val isWithinClickMovement = movedDistance <= CLICK_SLOP_THRESHOLD
                val isWithinClickTimeout = SystemClock.uptimeMillis() - clickStartMillis <= CLICK_TIMEOUT
                val isClicked = isWithinClickMovement && isWithinClickTimeout
                if (isClicked) this.performClick()
                else {
                    onRectangleMovementFinished()
                }
                touchedRectangle = null
            }
            MotionEvent.ACTION_CANCEL -> {
                // Restore the original rectangle coordinates to the moved rect
                touchedRectangle?.apply {
                    val originalRect = originalTouchedRectangle!!
                    x = originalRect.x
                    y = originalRect.y
                }?.also {
                    invalidate()
                }
                onRectangleMovementFinished(canceled = true)
                touchedRectangle = null
            }
        }
        return true
    }

    /**
     * Requests disallow intercept of touch events from the parent view and
     * performs a haptic feedback to user about the movement
     */
    private fun onRectangleMovementStarted() {
        parent.requestDisallowInterceptTouchEvent(true)
        performHapticFeedback(
            HapticFeedbackConstants.VIRTUAL_KEY,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    /**
     * Asks parent to continue intercept touches and gives the rejection
     * feedback to user when the movement is cancelled by any reason
     *
     * In case of acceptance calls the update callback with moved rectangle
     *
     * @param canceled `true` when the move gesture is cancelled `false` otherwise
     */
    private fun onRectangleMovementFinished(canceled: Boolean = false) {
        parent.requestDisallowInterceptTouchEvent(false)
        if (canceled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) performHapticFeedback(
                HapticFeedbackConstants.REJECT,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        } else updateListener?.onRectangleUpdated(touchedRectangle!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw all the rectangles
        rectangles.forEach { rectangle ->
            canvas.drawRectangle(rectangle)
        }
    }

    private fun Canvas.drawRectangle(rect: RectangleItemModel) {
        rectDrawable.apply {
            setBounds(
                rect.leftBound.roundToInt(),
                rect.topBound.roundToInt(),
                rect.rightBound.roundToInt(),
                rect.bottomBound.roundToInt(),
            )
        }.draw(this)
    }

    private infix operator fun RectF.contains(point: PointF) = point.x in left..right && point.y in top..bottom

    private val RectangleItemModel.centerX get() = x * width
    private val RectangleItemModel.centerY get() = y * height
    private val RectangleItemModel.widthInPx get() = size * width
    private val RectangleItemModel.heightInPx get() = size * height
    private val RectangleItemModel.leftBound get() = centerX - widthInPx / 2
    private val RectangleItemModel.topBound get() = centerY - heightInPx / 2
    private val RectangleItemModel.rightBound get() = centerX + widthInPx / 2
    private val RectangleItemModel.bottomBound get() = centerY + heightInPx / 2

    /**
     * Rectangles update listener
     */
    fun interface UpdateListener {
        /**
         * Gets called each time a rectangle is moved by user.
         *
         * @param rectangle The moved rectangle with updated properties
         */
        fun onRectangleUpdated(rectangle: RectangleItemModel)
    }
}