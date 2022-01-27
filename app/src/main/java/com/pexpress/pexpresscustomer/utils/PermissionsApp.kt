package com.pexpress.pexpresscustomer.utils

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

object PermissionsApp {


    fun requestLocationPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            requestCode
        )
    }
}