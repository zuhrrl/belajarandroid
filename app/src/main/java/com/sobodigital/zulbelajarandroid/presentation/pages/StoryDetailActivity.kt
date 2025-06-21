package com.sobodigital.zulbelajarandroid.presentation.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.StoryDetailBinding
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.StoryDetailViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.StoryDetailViewModelFactory

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: StoryDetailBinding
    var eventItem: EventItem? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val storyId = intent.getStringExtra("id")

        binding = StoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: StoryDetailViewModelFactory = StoryDetailViewModelFactory.getInstance(this)
        val viewModel: StoryDetailViewModel by viewModels { factory }

        storyId.let {
            id -> Log.d("TEST", id.toString())
            viewModel.fetchEventById(id!!)
        }

        viewModel.isLoading.observe(this) {isLoading ->
            if(isLoading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        viewModel.errorMessage.observe(this) {message ->
            Log.d("EventDetailActivity", message)
            if(message.isNotEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        viewModel.story.observe(this) {data ->
            val item = data
            Glide.with(baseContext).load(item.photoUrl).into(binding.detailImage)
            binding.title.text = item.name
            binding.description.text = item.description
        }

    }
}