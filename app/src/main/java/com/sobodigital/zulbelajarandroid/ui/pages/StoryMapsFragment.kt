package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.databinding.FragmentStoryMapsBinding
import com.sobodigital.zulbelajarandroid.viewmodel.StoryMapsViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.StoryMapsViewModelFactory

class StoryMapsFragment : Fragment() {

    private lateinit var viewModel: StoryMapsViewModel
    private var isMapReady : Boolean = false

    private val callback = OnMapReadyCallback { googleMap ->
        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        googleMap.addMarker(
            MarkerOptions()
                .position(dicodingSpace)
                .title("Dicoding Space")
                .snippet("Batik Kumeli No.50"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStoryMapsBinding.inflate(layoutInflater)

        val factory: StoryMapsViewModelFactory = StoryMapsViewModelFactory
            .getInstance(requireContext())
        val storyMapsViewModel: StoryMapsViewModel by viewModels { factory }
        viewModel = storyMapsViewModel
        viewModel.initMapsCallback()
        viewModel.fetchStoryWithLocation()

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if(isLoading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        viewModel.errorData.observe(viewLifecycleOwner) { data ->
            val message = data?.error ?: return@observe

            if(message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        viewModel.isMapReady.observe(viewLifecycleOwner) { isReady ->
          isMapReady = isReady
        }

        viewModel.listStory.observe(viewLifecycleOwner) { stories ->
            if(isMapReady) {
                viewModel.addMarkerToAllStoryLocation(stories)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(viewModel.callback)
        Log.d(TAG, "onViewCreated")

    }

    companion object {
        private var TAG = StoryMapsFragment::class.java.simpleName
    }
}