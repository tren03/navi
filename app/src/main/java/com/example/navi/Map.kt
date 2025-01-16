package com.example.navi

import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class Map : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val imageView = findViewById<SubsamplingScaleImageView>(R.id.imageView)
        val pinOverlay = findViewById<PinOverlayView>(R.id.pinOverlay)

        // 🔹 Set ImageView reference before loading image
        pinOverlay.setImageView(imageView)

        // Load the floor plan image
        imageView.setImage(ImageSource.resource(R.drawable.small_map))

        // Ensure the pin is set AFTER the image is loaded
        imageView.setOnImageEventListener(object : SubsamplingScaleImageView.OnImageEventListener {
            override fun onReady() {
                // 🔹 Set pin at (0,0), which is bottom-left corner
                pinOverlay.setPinLocation(1000f, 1000f)
            }

            override fun onImageLoaded() {}
            override fun onPreviewLoadError(e: Exception?) {}
            override fun onImageLoadError(e: Exception?) {}
            override fun onTileLoadError(e: Exception?) {}
            override fun onPreviewReleased() {}
        })
    }
}
