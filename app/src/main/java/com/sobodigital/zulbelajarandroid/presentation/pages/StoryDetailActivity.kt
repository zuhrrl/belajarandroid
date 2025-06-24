package com.sobodigital.zulbelajarandroid.presentation.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.StoryDetailBinding
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.StoryDetailViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.StoryDetailViewModelFactory

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: StoryDetailBinding
    private var story: Story? = null

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
            viewModel.fetchStoryById(id!!)
            viewModel.checkIsFavoriteStory(id)
        }

        viewModel.isLoading.observe(this) {isLoading ->
            if(isLoading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        viewModel.isFavoriteLoading.observe(this) {isLoading ->
            if(isLoading) {
                binding.btnFavorite.visibility = View.GONE
                binding.favoriteLoading.visibility = View.VISIBLE
                return@observe
            }
            binding.btnFavorite.visibility = View.VISIBLE
            binding.favoriteLoading.visibility = View.GONE
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

        viewModel.story.observe(this) { data ->
            Glide.with(baseContext).load(data.photoUrl).into(binding.detailImage)
            binding.title.text = data.name
            binding.description.text = data.description
            story = data
            binding.btnFavorite.visibility = View.VISIBLE
        }

        viewModel.isFavorite.observe(this) {isFavorite ->
            if(isFavorite) {
                binding.btnFavorite.setBackgroundResource(R.drawable.favorite_24fill)
                binding.btnFavorite.background.setTint(ContextCompat.getColor(this, R.color.pink))
                return@observe
            }
            binding.btnFavorite.setBackgroundResource(R.drawable.favorite_24px)
            binding.btnFavorite.background.setTint(ContextCompat.getColor(this, R.color.grey))
        }


        binding.btnFavorite.setOnClickListener {
            story?.let {
                viewModel.bookmarkStory(it)
            }
        }

    }
}