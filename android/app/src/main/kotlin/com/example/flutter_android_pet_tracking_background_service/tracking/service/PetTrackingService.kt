package com.example.flutter_android_pet_tracking_background_service.tracking.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.flutter_android_pet_tracking_background_service.R
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker

class PetTrackingService : Service(), TrackingService {
    private var listener: PetTrackingListener? = null

    override fun onBind(intent: Intent): IBinder = LocalBinder()

    override fun start() {
        startForegroundNotification()
        for (i in 0..10){
            listener?.testData("Index $i")
        }
    }

    override fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun attachListener(listener: PetTrackingListener?) {
        this.listener = listener
    }

    private fun startForegroundNotification() {
        if (VersionChecker.isGreaterThanOrEqualToOreo()) {
            val builder: Notification.Builder =
                    Notification.Builder(this, "sriharsha")
                            .setContentText("Pet tracking mode")
                            .setContentTitle("Background service")
                            .setSmallIcon(R.drawable.launch_background)
            startForeground(143, builder.build())
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): TrackingService = this@PetTrackingService
    }
}