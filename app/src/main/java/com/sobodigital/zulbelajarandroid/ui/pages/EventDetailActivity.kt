package com.sobodigital.zulbelajarandroid.ui.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.EventDetailBinding
import com.sobodigital.zulbelajarandroid.databinding.MainActivityBinding
import com.sobodigital.zulbelajarandroid.viewmodel.EventDetailViewModel

class EventDetailActivity : ComponentActivity() {
    private lateinit var binding: EventDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventId = intent.getIntExtra("event_id", 0)


        binding = EventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(EventDetailViewModel::class.java)

        eventId.let {
            id -> Log.d("TEST", id.toString())
            eventDetailViewModel.fetchEventById(id)
        }

        eventDetailViewModel.isLoading.observe(this) {isLoading ->
            if(isLoading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        eventDetailViewModel.errorMessage.observe(this) {message ->
            Log.d("EventDetailActivity", message)
            if(message.isNotEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        eventDetailViewModel.event.observe(this) {data ->
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
        }

        binding.btnFavorite.setOnClickListener {
            binding.btnFavorite.setBackgroundResource(R.drawable.favorite_24fill)
            binding.btnFavorite.background.setTint(ContextCompat.getColor(this, R.color.pink))
        }

    }
}