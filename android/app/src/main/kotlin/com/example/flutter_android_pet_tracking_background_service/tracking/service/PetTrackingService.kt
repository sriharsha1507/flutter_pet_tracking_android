package com.example.flutter_android_pet_tracking_background_service.tracking.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.flutter_android_pet_tracking_background_service.R
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker

class PetTrackingService : Service(), TrackingService {
    private lateinit var listener: PetTrackingListener

    override fun onCreate() {
        super.onCreate()
        startForegroundNotification()
    }

    override fun onBind(intent: Intent): IBinder = LocalBinder()

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attachListener(listener: PetTrackingListener) {
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