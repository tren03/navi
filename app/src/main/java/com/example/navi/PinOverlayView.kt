package com.example.navi

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class PinOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL  // Filled circle (solid dot)
    }

    private var pinPoint: PointF? = null  // Real-world coordinates of the pin
    private var imageView: SubsamplingScaleImageView? = null  // Reference to SSIV

    fun setImageView(imageView: SubsamplingScaleImageView) {
        this.imageView = imageView
        invalidate()  // Force redraw
    }

    fun setPinLocation(realX: Float, realY: Float) {
        pinPoint = PointF(realX, realY)
        invalidate()  // Request redraw
    }

    fun mapToSSIVCoordinates(gridX: Float, gridY: Float, gridMaxX: Int, gridMaxY: Int): PointF? {
        if (imageView == null || !imageView!!.isReady) {
            Log.e("PinOverlayView", "ImageView is not ready yet")
            return null
        }

        val imageWidth = imageView!!.sWidth.toFloat()
        val imageHeight = imageView!!.sHeight.toFloat()

        val scaleX = imageWidth / gridMaxX  // Scale factor for X
        val scaleY = imageHeight / gridMaxY // Scale factor for Y

        val imageX = gridX * scaleX
        val imageY = gridY * scaleY

        return imageView!!.sourceToViewCoord(imageX, imageY)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (imageView == null || pinPoint == null) {
            Log.e("PinOverlayView", "Missing required data: imageView or pinPoint is null")
            return
        }

        if (!imageView!!.isReady) {
            Log.e("PinOverlayView", "imageView is not ready yet")
            return
        }

        val scaledCoords = mapToSSIVCoordinates(pinPoint!!.x, pinPoint!!.y, 500, 500)

        if (scaledCoords == null) {
            Log.e("PinOverlayView", "Mapping to SSIV coordinates failed")
            return
        }

        val x = scaledCoords.x
        val y = scaledCoords.y

        Log.d("PinOverlayView", "Drawing blue dot at View coordinates: x=$x, y=$y")

        // 🔵 Draw a filled blue circle instead of bitmap
        canvas.drawCircle(x, y, 10f, paint) // 10f is the radius of the dot
    }
}
