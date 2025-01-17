package com.example.navi

import android.os.Handler
import android.os.Looper

class PinAnimator(
    private val pinOverlay: PinOverlayView,
    private val maxX: Float = 500f,
    private val maxY: Float = 500f,
    private val step: Float = 10f, // Movement speed
    private val interval: Long = 500 // Update every 500ms
) {
    private val handler = Handler(Looper.getMainLooper())
    private var xPos = 0f
    private var yPos = 0f
    private var isRunning = false

    private val movePinRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return

            // Update position
            xPos = (xPos + step) % maxX
            yPos = (yPos + step) % maxY

            pinOverlay.setPinLocation(xPos, yPos) // Move pin

            handler.postDelayed(this, interval) // Run again after interval
        }
    }

    fun startAnimation() {
        if (!isRunning) {
            isRunning = true
            handler.post(movePinRunnable)
        }
    }

    fun stopAnimation() {
        isRunning = false
        handler.removeCallbacks(movePinRunnable)
    }
}
