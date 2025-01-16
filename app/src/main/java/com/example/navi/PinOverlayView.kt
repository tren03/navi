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

    fun givePixelValue(realX:Float,realY:Float){
    }

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

        // 🔹 Step 1: Get actual image dimensions in pixels
        val imageWidth = imageView!!.sWidth.toFloat()
        val imageHeight = imageView!!.sHeight.toFloat()

        Log.d("PinOverlayView", "Image Size: Width=$imageWidth, Height=$imageHeight")

        // 🔹 Step 2: Compute scale factor for non-square grid
        val scaleX = imageWidth / gridMaxX  // Scale factor for X
        val scaleY = imageHeight / gridMaxY // Scale factor for Y

        Log.d("PinOverlayView", "Scale Factors: scaleX=$scaleX, scaleY=$scaleY")

        // 🔹 Step 3: Convert grid coordinates to image coordinates
        val imageX = gridX * scaleX
        val imageY = gridY * scaleY

        Log.d("PinOverlayView", "Grid to Image: gridX=$gridX, gridY=$gridY → imageX=$imageX, imageY=$imageY")

        // 🔹 Step 4: Convert image space coordinates to view space (SSIV coordinates)
        val viewCoord = imageView!!.sourceToViewCoord(imageX, imageY)

        if (viewCoord == null) {
            Log.e("PinOverlayView", "sourceToViewCoord returned null")
            return null
        }

        Log.d("PinOverlayView", "Image to View: imageX=$imageX, imageY=$imageY → viewX=${viewCoord.x}, viewY=${viewCoord.y}")

        return viewCoord
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
        Log.d("PinOverlayView","IMG SIZE WIDTH : ${imageSize.first} HEIGHT : ${imageSize.second}")

        val scaledCoords = mapToSSIVCoordinates(pinPoint!!.x,pinPoint!!.y,500,500)
        Log.d("PinOverlayView","scaled coords : ${scaledCoords}")

        val viewCoord = imageView!!.sourceToViewCoord(pinPoint!!.x, pinPoint!!.y)
        Log.d("PinOverlayView","VIEW COORD X : ${viewCoord!!.x} Y : ${viewCoord!!.y}")


        if (viewCoord == null) {
            Log.e("PinOverlayView", "sourceToViewCoord returned null")
            return
        }

        //val x = viewCoord.x - (pinBitmap!!.width/2)
        //val y = viewCoord.y - pinBitmap!!.height

        val x = scaledCoords!!.x - (pinBitmap!!.width/2)
        val y = scaledCoords!!.y - pinBitmap!!.height

        Log.d("PinOverlayView", "Drawing pin at View coordinates: x=$x, y=$y")
        canvas.drawBitmap(pinBitmap!!, x, y, paint)
    }
}
