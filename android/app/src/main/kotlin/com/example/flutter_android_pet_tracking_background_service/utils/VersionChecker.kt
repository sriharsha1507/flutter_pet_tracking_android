package com.example.flutter_android_pet_tracking_background_service.utils

import android.os.Build

object VersionChecker {
    fun isGreaterThanOrEqualToOreo() =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}