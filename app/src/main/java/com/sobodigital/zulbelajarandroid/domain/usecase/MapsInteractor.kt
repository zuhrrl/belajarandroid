package com.sobodigital.zulbelajarandroid.domain.usecase

import com.google.android.gms.maps.GoogleMap
import com.sobodigital.zulbelajarandroid.domain.model.LocationData
import com.sobodigital.zulbelajarandroid.domain.repository.MapsRepository

class MapsInteractor(private val mapsRepository: MapsRepository) : MapsUseCase {
    override fun setGoogleMap(map: GoogleMap) {
        return mapsRepository.setGoogleMap(map)
    }

    override fun addMarker(name: String, locationData: LocationData) {
        return mapsRepository.addMarker(name, locationData)
    }
}