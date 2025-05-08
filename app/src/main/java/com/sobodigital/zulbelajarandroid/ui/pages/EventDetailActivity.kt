package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.Destination
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.EventDetailBinding
import com.sobodigital.zulbelajarandroid.databinding.MainActivityBinding
import com.sobodigital.zulbelajarandroid.viewmodel.EventDetailViewModel

class EventDetailActivity : ComponentActivity() {
    private lateinit var binding: EventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventId = intent.getIntExtra("event_id", 0)


        binding = EventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(EventDetailViewModel::class.java)

        eventId.let {
            id -> Log.d("TEST", id.toString())
            eventDetailViewModel.fetchEventById(id) }

//        Glide.with(baseContext).load(data?.imageLogo).into(binding.detailImage)
//        binding.title.text = data?.name
//        binding.description.text = HtmlCompat.fromHtml(
//            data?.description.toString(),
//            HtmlCompat.FROM_HTML_MODE_LEGACY
//        )

    }
}