package com.sobodigital.zulbelajarandroid

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class DestinationDetailActivity : ComponentActivity() {
    private lateinit var coverImage: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getParcelableExtra("data", Destination::class.java)
        setContentView(R.layout.destination_detail)

        coverImage = findViewById(R.id.detail_image)
        title = findViewById(R.id.dest_detail_title)
        description = findViewById(R.id.destination_description)

        coverImage.setImageResource(data!!.image)
        title.text = data.name
        description.text = data.description

    }
}