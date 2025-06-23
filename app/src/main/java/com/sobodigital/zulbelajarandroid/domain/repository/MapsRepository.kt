package com.sobodigital.zulbelajarandroid.domain.repository

import com.google.android.gms.maps.GoogleMap
import com.sobodigital.zulbelajarandroid.domain.model.LocationData

interface MapsRepository {

    fun setGoogleMap(map: GoogleMap)

    fun addMarker(name: String, locationData: LocationData)
}