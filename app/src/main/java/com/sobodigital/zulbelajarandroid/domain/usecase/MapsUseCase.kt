package com.sobodigital.zulbelajarandroid.domain.usecase

import com.google.android.gms.maps.GoogleMap
import com.sobodigital.zulbelajarandroid.domain.model.LocationData

interface MapsUseCase {

    fun setGoogleMap(map: GoogleMap)

    fun addMarker(name: String, locationData: LocationData)

}