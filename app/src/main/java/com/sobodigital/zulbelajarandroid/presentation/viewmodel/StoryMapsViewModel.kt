package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StoryMapsViewModel(private val mapsUseCase: MapsUseCase, private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _listStory = MutableLiveData<List<Story>>()
    val listStory = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isMapReady = MutableLiveData<Boolean>()
    val isMapReady: LiveData<Boolean> = _isMapReady

    private val _errorData = MutableLiveData<Result.Error?>()
    val errorData = _errorData

    lateinit var callback: OnMapReadyCallback
    private var googleMap: GoogleMap? = null

    fun initMapsCallback() {
        _isMapReady.value = false
        callback = OnMapReadyCallback { maps ->
            googleMap = maps
            mapsUseCase.setGoogleMap(maps)
            googleMap?.uiSettings?.isZoomControlsEnabled = true

            _isMapReady.value = true
         }
    }

    fun fetchStoryWithLocation() {
        viewModelScope.launch {
            _errorData.value = Result.Error("")
            _isLoading.value = true
            when(val response = storyUseCase.fetchStories(1)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorData.value = response
                }
                is Result.Success -> {
                    _isLoading.value = false
                    Log.d(TAG, response.data.toString())
                    _listStory.value = response.data
                }
                null -> {
                    _isLoading.value = false
                    _errorData.value = Result.Error("Error: Unimplemented!")
                }
            }
        }
    }

    fun addMarkerToAllStoryLocation(list: List<Story>) {
        if(list.isEmpty()) {
            _errorData.value = Result.Error("Cant add marker, stories is empty!")
            return
        }

        for(item in list) {
            if(item.lat != null && item.lon != null) {
                addMarker(item)
            }
        }
    }

    private fun addMarker(story: Story) {
        _isLoading.value = true
        val lat = story.lat as Double
        val lon = story.lon as Double

        if(googleMap == null) {
            _errorData.value = Result.Error("Cant add marker, please wait maps is not ready!")
            return
        }

        viewModelScope.launch {
            mapsUseCase.addMarker(story.name ?: "Unknown", LatLng(lat, lon))
            delay(1000)
            _isLoading.value = false
        }

    }

    companion object {
        private var TAG = StoryMapsViewModel::class.java.simpleName
    }

}