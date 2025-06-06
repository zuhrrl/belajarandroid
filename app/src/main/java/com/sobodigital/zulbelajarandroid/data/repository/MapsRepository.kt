package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsRepository(private val context: Context) {
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var googleMap: GoogleMap

    fun setGoogleMap(map: GoogleMap) {
        googleMap = map
    }

    fun addMarker(name: String, latLng: LatLng) {
        googleMap.addMarker(MarkerOptions().position(latLng).title(name))
        boundsBuilder.include(latLng)

        val bounds: LatLngBounds = boundsBuilder.build()
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
               context.resources.displayMetrics.widthPixels,
               context.resources.displayMetrics.heightPixels,
                300
            ),
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    googleMap.animateCamera(CameraUpdateFactory.zoomBy(2f))
                }
                override fun onCancel() {
                }
            }
        )
    }

    companion object {
        private  val TAG = MapsRepository::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: MapsRepository? = null
        fun getInstance(
            context: Context,
            ): MapsRepository =
            instance ?: synchronized(this) {
                instance ?: MapsRepository(context)
            }.also { instance = it }
    }


}