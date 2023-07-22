package com.hirno.rectangles.ui.main

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.ui.view.RectanglesView


/**
 * [BindingAdapter]s for the [MainFragment].
 */
@BindingAdapter("app:model")
fun setModel(rectanglesView: RectanglesView, model: RectanglesResponseModel?) {
    model?.let {
        rectanglesView.rectangles = it.rectangles
    }
}

@BindingAdapter("android:text")
fun setText(textView: TextView, text: Any?) = with(textView) {
    when (text) {
        is Int -> setText(text)
        is String? -> setText(text)
    }
}