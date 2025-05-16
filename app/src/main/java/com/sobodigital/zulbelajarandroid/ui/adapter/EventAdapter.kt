package com.sobodigital.zulbelajarandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.databinding.ItemEventBinding

class EventAdapter(private val events: List<EventItem>) :
   ListAdapter<EventItem, EventAdapter.ListEventViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventItem>() {
            override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListEventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListEventViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: EventItem)
    }

    class ListEventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: EventItem){
            Glide.with(binding.root).load(review.imageLogo).into(binding.imgEvent)
            binding.title.text = review.name
        }
    }
}
