package com.sobodigital.zulbelajarandroid.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.MainActivityBinding
import com.sobodigital.zulbelajarandroid.ui.adapter.EventAdapter
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var rvDestinations: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: MainActivityBinding
    private var listEvent = listOf<EventItem>()
    private lateinit var eventMainViewModel: EventMainViewModel

    companion object{
        private const val TAG = "MainActivity"
    }


    private fun showRecyclerList(list: List<EventItem>) {
        rvDestinations.layoutManager = LinearLayoutManager(this)
        val adapter = EventAdapter(list)
        rvDestinations.adapter = adapter

        adapter.setOnItemClickCallback(object: EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EventItem) {
                Log.d("debug", "Okee click")
                val intent = Intent(this@MainActivity, EventDetailActivity::class.java)
                intent.putExtra("event_id", data.id)
                startActivity(intent)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvDestinations = binding.rvEvent
        bottomNav = binding.bottomNavigationView

        eventMainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(EventMainViewModel::class.java)

        eventMainViewModel.fetchListEvent(1)

        eventMainViewModel.listEvent.observe(this) { data ->
            Log.d(TAG, "OBSERVER $data")
            listEvent = data
            showRecyclerList(listEvent)
        }

        eventMainViewModel.isLoading.observe(this) { isloading ->
            if(isloading) {
                binding.homeLoading.visibility = View.VISIBLE
                return@observe
            }
            binding.homeLoading.visibility = View.GONE
            return@observe
        }

        eventMainViewModel.errorMessage.observe(this) {message ->
            Log.d("MainActivity", message)
            if(message.isNotEmpty() && listEvent.isEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        Log.d("DEV", listEvent.size.toString())


        bottomNav.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.upcoming_event -> {

                }
                R.id.event_finished -> {
                    val intent = Intent(this@MainActivity, EventFinishedActivity::class.java)
                    startActivity(intent)
                }

            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        eventMainViewModel.fetchListEvent(1)
        bottomNav.selectedItemId = R.id.upcoming_event

    }
}