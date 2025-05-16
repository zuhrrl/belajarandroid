package com.sobodigital.zulbelajarandroid.ui.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.databinding.EventDetailBinding
import com.sobodigital.zulbelajarandroid.viewmodel.EventDetailViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.EventDetailViewModelFactory

class EventDetailActivity : ComponentActivity() {
    private lateinit var binding: EventDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventId = intent.getIntExtra("event_id", 0)

        binding = EventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: EventDetailViewModelFactory = EventDetailViewModelFactory.getInstance(this)
        val viewModel: EventDetailViewModel by viewModels { factory }

        eventId.let {
            id -> Log.d("TEST", id.toString())
            viewModel.fetchEventById(id)
        }

        viewModel.isLoading.observe(this) {isLoading ->
            if(isLoading) {
                binding.btnFavorite.visibility = View.GONE
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

        viewModel.event.observe(this) {data ->
            val quota = data?.quota!! - data.registrants!!
            Glide.with(baseContext).load(data.imageLogo).into(binding.detailImage)
            binding.title.text = data.name
            binding.owner.text = "Penyelenggara: ${data.ownerName}"
            binding.beginTime.text = "Dimulai: ${data.beginTime}"
            binding.quotaAndRegistrant.text = "Sisa kuota: $quota"
            binding.description.text = HtmlCompat.fromHtml(
                data.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.btnOpenLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                startActivity(intent)
            }
            binding.btnFavorite.visibility = View.VISIBLE
        }

        binding.btnFavorite.setOnClickListener {
            binding.btnFavorite.setBackgroundResource(R.drawable.favorite_24fill)
            binding.btnFavorite.background.setTint(ContextCompat.getColor(this, R.color.pink))
        }

    }
}