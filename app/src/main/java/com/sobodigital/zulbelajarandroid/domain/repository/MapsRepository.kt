package com.sobodigital.zulbelajarandroid.domain.repository

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface MapsRepository {

    fun setGoogleMap(map: GoogleMap)

    fun addMarker(name: String, latLng: LatLng)
}