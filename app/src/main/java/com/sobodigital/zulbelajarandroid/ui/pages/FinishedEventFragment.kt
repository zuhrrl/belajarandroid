package com.sobodigital.zulbelajarandroid.ui.pages

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
import com.sobodigital.zulbelajarandroid.databinding.FragmentUpcomingEventBinding
import com.sobodigital.zulbelajarandroid.ui.adapter.EventAdapter
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModelFactory

class FinishedEventFragment : Fragment() {

    private lateinit var eventRecyclerView: RecyclerView
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

        val factory: EventMainViewModelFactory = EventMainViewModelFactory.getInstance(requireActivity())
        val eventMainViewModel: EventMainViewModel by viewModels { factory }

        eventMainViewModel.fetchListEvent(0)
        eventMainViewModel.listEvent.observe(viewLifecycleOwner) { data ->
            Log.d(TAG, "OBSERVER $data")
            listEvent = data
            showRecyclerList(listEvent)
        }

        eventMainViewModel.isLoading.observe(viewLifecycleOwner) { isloading ->
            if(isloading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        eventMainViewModel.errorMessage.observe(viewLifecycleOwner) {message ->
            Log.d("MainActivity", message)
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

    companion object {
        private const val TAG = "FinishedEventFragment"
    }
}