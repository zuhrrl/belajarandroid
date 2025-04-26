package com.sobodigital.zulbelajarandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DestinationAdapter(private val destinations: MutableList<Destination>) :
    RecyclerView.Adapter<ListViewHolderDestination>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolderDestination {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_destination, parent, false)
        return ListViewHolderDestination(view)
    }

    override fun getItemCount(): Int {
        return destinations.size
    }

    override fun onBindViewHolder(holder: ListViewHolderDestination, position: Int) {
        val item = destinations[position]
        holder.imgDestination.setImageResource(item.image)
        holder.title.text = item.name
        holder.description.text = item.description
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Destination)
    }
}
