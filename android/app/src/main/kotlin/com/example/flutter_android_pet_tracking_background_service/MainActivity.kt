package com.example.flutter_android_pet_tracking_background_service

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.flutter_android_pet_tracking_background_service.tracking.model.PathLocation
import com.example.flutter_android_pet_tracking_background_service.tracking.service.PetTrackingListener
import com.example.flutter_android_pet_tracking_background_service.tracking.service.PetTrackingService
import com.example.flutter_android_pet_tracking_background_service.tracking.service.TrackingService
import com.example.flutter_android_pet_tracking_background_service.utils.DartCall
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

private const val METHOD_CHANNEL = "DeveloperGundaChannel"

class MainActivity : FlutterActivity(), PetTrackingListener {
    private var trackingService: TrackingService? = null
    private lateinit var connection: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
        setUpMethodChannelListener()
    }

    override fun onResume() {
        super.onResume()
        bindService(object : PetTrackingServiceHandler {
            override fun onBound() {
                Log.e("SRI", "Bound Service")
                trackingService?.attachListener(this@MainActivity)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        trackingService?.attachListener(null)
        unbindService(connection)
    }

    override fun onNewLocation(location: PathLocation) {
        invokePathLocation(location)
    }

    private fun bindService(serviceHandler: PetTrackingServiceHandler) {
        connection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                trackingService = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as PetTrackingService.LocalBinder
                trackingService = binder.getService()
                serviceHandler.onBound()
            }
        }
        val intent = Intent(this, PetTrackingService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }


    private fun startPetTrackingService() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    143)
            return
        } else {
            trackingService?.start()
        }
    }

    private fun stopPetTrackingService() {
        trackingService?.stop()
    }

    private fun setUpMethodChannelListener() {
        MethodChannel(flutterView, METHOD_CHANNEL).setMethodCallHandler { methodCall, result ->
            if (methodCall.method == DartCall.START_PET_TRACKING) {
                startPetTrackingService()
                result.success("Yay!! Tracking Gunda Pet :) ")
            } else if (methodCall.method == DartCall.STOP_PET_TRACKING) {
                stopPetTrackingService()
                result.success("Gunda pet tracking stopped :) ")
            }
        }
    }

    private fun invokeMethod(testData: String) {
        MethodChannel(flutterView, METHOD_CHANNEL).invokeMethod(DartCall.TEST_PET_TRACKING, testData)
    }

    private fun invokePathLocation(pathLocation: PathLocation) {
        MethodChannel(flutterView, METHOD_CHANNEL).invokeMethod(DartCall.PATH_LOCATION, pathLocation.toString())
    }

    interface PetTrackingServiceHandler {
        fun onBound()
    }
}
