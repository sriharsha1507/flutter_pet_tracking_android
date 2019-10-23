package com.example.flutter_android_pet_tracking_background_service.tracking.service

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.flutter_android_pet_tracking_background_service.R
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker

private const val GPS_TRACKING_IN_MILLIS: Long = 1000
private const val GPS_TRACKING_IN_DISTANCE_METERS: Float = 1f

class PetTrackingService : Service(), TrackingService, LocationListener {
    private var listener: PetTrackingListener? = null
    private lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onBind(intent: Intent): IBinder = LocalBinder()

    override fun start() {
        startForegroundNotification()
        initPetLocationTracking()
    }

    override fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun attachListener(listener: PetTrackingListener?) {
        this.listener = listener
    }

    override fun onLocationChanged(location: Location) {
        Log.e("GUNDALOCATION", "latitude-${location.latitude} && longitude-${location.longitude}")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun getLocationCriteria(): Criteria {
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.POWER_HIGH
        criteria.isAltitudeRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.isBearingRequired = false
        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
        criteria.verticalAccuracy = Criteria.ACCURACY_HIGH
        return criteria
    }

    private fun initPetLocationTracking() {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                } else {
                    TODO("VERSION.SDK_INT < M")
                }) return
        locationManager.requestLocationUpdates(
                GPS_TRACKING_IN_MILLIS,
                GPS_TRACKING_IN_DISTANCE_METERS,
                getLocationCriteria(),
                this,
                null)
    }

    inner class LocalBinder : Binder() {
        fun getService(): TrackingService = this@PetTrackingService
    }
}