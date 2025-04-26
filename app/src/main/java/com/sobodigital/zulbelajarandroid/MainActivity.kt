package com.sobodigital.zulbelajarandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var rvDestinations: RecyclerView
    private var destinations: MutableList<Destination> = mutableListOf()

    private fun showRecyclerList(list: MutableList<Destination>) {
        rvDestinations.layoutManager = LinearLayoutManager(this)
        val adapter = DestinationAdapter(list)
        rvDestinations.adapter = adapter

        adapter.setOnItemClickCallback(object: DestinationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Destination) {
                Log.d("debug", "Okee click")
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        rvDestinations = findViewById(R.id.rv_destination)
        destinations = mutableListOf(Destination(name = "Test", 2, "Test desc 666"))

        showRecyclerList(destinations)
    }
}