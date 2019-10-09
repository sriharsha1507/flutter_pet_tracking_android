package com.example.flutter_android_pet_tracking_background_service

import android.util.Log
import com.example.flutter_android_pet_tracking_background_service.notification.service.NotificationChannelService
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker
import io.flutter.app.FlutterApplication

class TrackingApplication : FlutterApplication() {
    override fun onCreate() {
        super.onCreate()

        createChannelIfRequired()

        Log.e("SRI", "Hi there")
    }

    private fun createChannelIfRequired() {
        if (VersionChecker.isGreaterThanOrEqualToOreo()) {
            NotificationChannelService.createChannel(this)
        }
    }
}