package com.example.flutter_android_pet_tracking_background_service

import android.content.Intent
import android.os.Bundle
import com.example.flutter_android_pet_tracking_background_service.tracking.service.PetTrackingService
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)


    }

    private fun startPetTrackingService() {
        val intent = Intent(this, PetTrackingService::class.java)

        if (VersionChecker.isGreaterThanOrEqualToOreo()) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
