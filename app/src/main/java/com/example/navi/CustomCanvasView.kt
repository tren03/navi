package com.example.navi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomCanvasView(context: Context) : View(context) {
    private val paint = Paint().apply {
        //color = Color.RED
        //strokeWidth = 50f
    }
    private val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.thirtyfive_map) // Replace with your image

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // Draw a line
        // canvas.drawLine(100f, 100f, 400f, 400f, paint)

        // Draw a rectangle
        // paint.style = Paint.Style.STROKE
        // canvas.drawRect(100f, 100f, 300f, 300f, paint)
    }

}