package com.sobodigital.zulbelajarandroid.ui.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.FragmentFinishedEventBinding
import com.sobodigital.zulbelajarandroid.ui.adapter.EventAdapter
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModelFactory
import com.sobodigital.zulbelajarandroid.viewmodel.FavoriteEventViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.FavoriteEventViewModelFactory
import okhttp3.internal.notify

class FavoriteEventFragment : Fragment() {
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var favoriteViewModel: FavoriteEventViewModel
    private var listEvent = listOf<EventItem>()

    private fun showRecyclerList(list: List<EventItem>) {
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = EventAdapter(list)
        eventRecyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object: EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventItem) {
                val intent = Intent(context, EventDetailActivity::class.java)
                intent.putExtra("event_id", data.id)
                startActivity(intent)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFinishedEventBinding.inflate(layoutInflater)

        eventRecyclerView = binding.rvEvent

        val factory: FavoriteEventViewModelFactory = FavoriteEventViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteEventViewModel by viewModels { factory }
        favoriteViewModel = viewModel

        viewModel.fetchFavoriteEvent()
        viewModel.listEvent.observe(viewLifecycleOwner) { data ->
            Log.d(TAG, "OBSERVER $data")
            listEvent = data
            if(listEvent.isEmpty()) {
                binding.errorMessage.text = "No data!"
                binding.errorMessage.visibility = View.VISIBLE
            }
            showRecyclerList(listEvent)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isloading ->
            if(isloading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {message ->
            Log.d(TAG, message)
            if(message.isNotEmpty() && listEvent.isEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.fetchFavoriteEvent()
    }

    companion object {
        private  val TAG = FavoriteEventViewModel::class.simpleName
    }

}