package com.sobodigital.zulbelajarandroid.domain.usecase

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.sobodigital.zulbelajarandroid.domain.repository.MapsRepository

class MapsInteractor(private val mapsRepository: MapsRepository) : MapsUseCase {
    override fun setGoogleMap(map: GoogleMap) {
        return mapsRepository.setGoogleMap(map)
    }

    override fun addMarker(name: String, latLng: LatLng) {
        return mapsRepository.addMarker(name, latLng)
    }
}