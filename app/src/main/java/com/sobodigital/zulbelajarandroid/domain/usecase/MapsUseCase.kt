package com.sobodigital.zulbelajarandroid.domain.usecase

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface MapsUseCase {

    fun setGoogleMap(map: GoogleMap)

    fun addMarker(name: String, latLng: LatLng)

}