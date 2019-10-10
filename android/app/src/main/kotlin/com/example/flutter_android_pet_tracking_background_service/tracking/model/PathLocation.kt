package com.example.flutter_android_pet_tracking_background_service.tracking.model

import android.location.Location

data class PathLocation(
        private val latitude: Double,
        private val longitude: Double
) {
    companion object {
        fun fromLocation(location: Location) =
                PathLocation(location.latitude, location.longitude)
    }
}