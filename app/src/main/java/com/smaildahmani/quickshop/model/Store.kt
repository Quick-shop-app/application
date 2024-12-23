package com.smaildahmani.quickshop.model

import com.google.android.gms.maps.model.LatLng

data class Store(
    val name: String,
    val address: String,
    val coordinates: LatLng
)

