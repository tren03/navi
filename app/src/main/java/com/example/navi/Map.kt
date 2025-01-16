package com.example.navi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class Map : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Find the SubsamplingScaleImageView
        val imageView = findViewById<SubsamplingScaleImageView>(R.id.imageView)

        // Load image from resources
        imageView.setImage(ImageSource.resource(R.drawable.small_map))

    }
}
