package com.pexpress.pexpresscustomer.utils

import com.google.android.libraries.places.api.model.Place

object UtilsMaps {
    val placeFields =
        listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG
        )
}