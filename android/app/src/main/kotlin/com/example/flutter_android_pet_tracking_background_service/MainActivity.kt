package com.example.flutter_android_pet_tracking_background_service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.flutter_android_pet_tracking_background_service.tracking.model.PathLocation
import com.example.flutter_android_pet_tracking_background_service.tracking.service.PetTrackingListener
import com.example.flutter_android_pet_tracking_background_service.tracking.service.PetTrackingService
import com.example.flutter_android_pet_tracking_background_service.tracking.service.TrackingService
import com.example.flutter_android_pet_tracking_background_service.utils.DartCall
import com.example.flutter_android_pet_tracking_background_service.utils.VersionChecker
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

private const val METHOD_CHANNEL = "DeveloperGundaChannel"

class MainActivity : FlutterActivity(), PetTrackingListener {
    private lateinit var trackingService: TrackingService
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
                trackingService.attachListener(this@MainActivity)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    override fun onNewLocation(location: PathLocation) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun testData(testData: String) {
        invokeMethod(testData)
    }

    private fun bindService(serviceHandler: PetTrackingServiceHandler) {
        connection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as PetTrackingService.LocalBinder
                trackingService = binder.getService()
                serviceHandler.onBound()
            }
        }
    }


    private fun startPetTrackingService() {
        val intent = Intent(this, PetTrackingService::class.java)

        if (VersionChecker.isGreaterThanOrEqualToOreo()) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun setUpMethodChannelListener() {
        MethodChannel(flutterView, METHOD_CHANNEL).setMethodCallHandler { methodCall, result ->
            if (methodCall.method == DartCall.START_PET_TRACKING) {
                startPetTrackingService()
                result.success("Yay!! Tracking Gunda Pet :) ")
            }
        }
    }

    private fun invokeMethod(testData: String) {
        MethodChannel(flutterView, METHOD_CHANNEL).invokeMethod(DartCall.TEST_PET_TRACKING, testData)
    }

    interface PetTrackingServiceHandler {
        fun onBound()
    }
}
