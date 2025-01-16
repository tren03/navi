package com.example.navi

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class PinOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var pinBitmap: Bitmap? = null
    private var pinPoint: PointF? = null  // Real-world coordinates of the pin
    private var imageView: SubsamplingScaleImageView? = null  // Reference to SSIV

    init {
        // Load the pin image from drawable (Ensure this exists in res/drawable/)
        pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.blue_dot_small)
    }

    fun setImageView(imageView: SubsamplingScaleImageView) {
        this.imageView = imageView
        invalidate()  // Force redraw
    }

    fun setPinLocation(realX: Float, realY: Float) {
        pinPoint = PointF(realX, realY)
        invalidate()  // Request redraw
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (imageView == null || pinPoint == null || pinBitmap == null) {
            Log.e("PinOverlayView", "Missing required data: imageView or pinPoint or pinBitmap is null")
            return
        }

        if (!imageView!!.isReady) {
            Log.e("PinOverlayView", "imageView is not ready yet")
            return
        }

        // Convert real-world coordinates to view coordinates
        val imageSize = imageView!!.sWidth to imageView!!.sHeight // Image dimensions
        val viewCoord = imageView!!.sourceToViewCoord(pinPoint!!.x, imageSize.second - pinPoint!!.y)

        if (viewCoord == null) {
            Log.e("PinOverlayView", "sourceToViewCoord returned null")
            return
        }

        val x = viewCoord.x - (pinBitmap!!.width / 2)
        val y = viewCoord.y - pinBitmap!!.height

        Log.d("PinOverlayView", "Drawing pin at View coordinates: x=$x, y=$y")
        canvas.drawBitmap(pinBitmap!!, x, y, paint)
    }
}
