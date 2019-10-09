package com.example.flutter_android_pet_tracking_background_service.tracking.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.flutter_android_pet_tracking_background_service.R
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker

class PetTrackingService : Service() {

    override fun onCreate() {
        super.onCreate()

        if (VersionChecker.isGreaterThanOrEqualToOreo()) {
            val builder: Notification.Builder =
                    Notification.Builder(this, "sriharsha")
                            .setContentText("Pet tracking mode")
                            .setContentTitle("Background service")
                            .setSmallIcon(R.drawable.launch_background)
            startForeground(143, builder.build())
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}