package com.sobodigital.zulbelajarandroid

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListViewHolderDestination(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imgDestination: ImageView = itemView.findViewById(R.id.img_destination)
    val title: TextView = itemView.findViewById(R.id.destination_title)
    val description: TextView = itemView.findViewById(R.id.destination_description)
}

