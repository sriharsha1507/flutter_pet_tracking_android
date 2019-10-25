package com.example.flutter_android_pet_tracking_background_service.tracking.service

import com.example.flutter_android_pet_tracking_background_service.tracking.model.PathLocation

interface PetTrackingListener {
    fun onNewLocation(location: PathLocation)
}