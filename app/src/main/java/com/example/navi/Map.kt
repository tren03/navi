package com.example.navi

import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class Map : AppCompatActivity() {
    private lateinit var pinOverlay: PinOverlayView
    private lateinit var pinAnimator: PinAnimator  // Reference to PinAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val imageView = findViewById<SubsamplingScaleImageView>(R.id.imageView)
        pinOverlay = findViewById(R.id.pinOverlay)

        pinOverlay.setImageView(imageView)

        imageView.setImage(ImageSource.resource(R.drawable.thirtyfive_map))

        // Initialize the pin animator
        pinAnimator = PinAnimator(pinOverlay)

        imageView.setOnImageEventListener(object : SubsamplingScaleImageView.OnImageEventListener {
            override fun onReady() {
                pinAnimator.startAnimation()  // 🚀 Start pin animation when the image is ready
            }

            override fun onImageLoaded() {
                pinOverlay.invalidate()  // Redraw pin when image loads
            }

            override fun onPreviewLoadError(e: Exception?) {}
            override fun onImageLoadError(e: Exception?) {}
            override fun onTileLoadError(e: Exception?) {}
            override fun onPreviewReleased() {}
        })

        imageView.setOnStateChangedListener(object : SubsamplingScaleImageView.OnStateChangedListener {
            override fun onScaleChanged(scaleFactor: Float, origin: Int) {
                pinOverlay.invalidate()  // Redraw pin when zooming
            }

            override fun onCenterChanged(newCenter: PointF?, origin: Int) {
                pinOverlay.invalidate()  // Redraw pin when panning
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        pinAnimator.stopAnimation() // 🛑 Stop the animation when the activity is destroyed
    }
}
