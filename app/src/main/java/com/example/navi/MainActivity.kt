package com.example.navi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var wifiListView: ListView
    private lateinit var progressBar: ProgressBar

    // List to hold Wi-Fi network data (SSID + signal strength)
    private val wifiList = mutableListOf<String>()

    // Handler for scheduling repeated tasks (1-second interval)
    private val handler = Handler(Looper.getMainLooper())

    // BroadcastReceiver for Wi-Fi scan results
    private var wifiScanReceiver: BroadcastReceiver? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val SCAN_INTERVAL_MS = 1000L  // 1 second
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        wifiListView = findViewById(R.id.wifiListView)
        progressBar = findViewById(R.id.progressBar)
        val wifiTitle = findViewById<TextView>(R.id.wifiTitle)
        wifiTitle.text = "Wi-Fi Networks (updating every 1s)"

        // Get the WifiManager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Check location permission required for Wi-Fi scanning (Android 9+)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startWifiScanning()
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startWifiScanning()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Cannot scan Wi-Fi networks.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startWifiScanning() {
        // Enable Wi-Fi if disabled
        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(this, "Wi-Fi disabled... enabling it now.", Toast.LENGTH_SHORT).show()
            wifiManager.isWifiEnabled = true
        }

        // BroadcastReceiver to handle scan results
        wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Hide progress bar once we have data
                progressBar.visibility = View.GONE

                // Retrieve the scan results
                val results: List<ScanResult> = wifiManager.scanResults
                wifiList.clear()
                for (result in results) {
                    // SSID + signal strength in dBm
                    val wifiInfo = "${result.SSID} - Strength: ${result.level} dBm"
                    wifiList.add(wifiInfo)
                }

                updateUI()
            }
        }

        // Register the receiver for scan results
        registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        // Begin scanning cycle (immediate + periodic)
        handler.post(scanRunnable)
    }

    // This Runnable calls startScan() every second
    private val scanRunnable = object : Runnable {
        override fun run() {
            scanWifiNetworks()
            // Schedule next scan after 1 second
            handler.postDelayed(this, SCAN_INTERVAL_MS)
        }
    }

    private fun scanWifiNetworks() {
        progressBar.visibility = View.VISIBLE
        wifiManager.startScan()
    }

    private fun updateUI() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, wifiList)
        wifiListView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister BroadcastReceiver and stop handler callbacks to prevent leaks
        wifiScanReceiver?.let { unregisterReceiver(it) }
        handler.removeCallbacks(scanRunnable)
    }
}
