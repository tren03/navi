package com.example.navi

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class PinOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var imageRect: RectF? = null
    private val markers = mutableListOf<Pair<Float, Float>>()

    fun setImageBounds(rect: RectF) {
        imageRect = rect
        invalidate()
    }

    fun addMarker(x: Float, y: Float) {
        markers.add(Pair(x, y))
        invalidate()  // Redraw view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        imageRect?.let { rect ->
            // Ensure all markers are within image bounds
            for ((x, y) in markers) {
                if (x in rect.left..rect.right && y in rect.top..rect.bottom) {
                    canvas.drawCircle(x, y, 10f, paint) // Draw red dot inside the image
                }
            }
        }
    }
}
