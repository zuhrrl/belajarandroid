package com.sobodigital.zulbelajarandroid.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.databinding.ItemStoryBinding

class StoryAdapter(private val stories: List<Story>) :
   ListAdapter<Story, StoryAdapter.ListEventViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEventViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListEventViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListEventViewHolder, position: Int) {
        val item = stories[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    class ListEventViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story){
            Glide.with(binding.root).load(item.photoUrl).into(binding.imgEvent)
            binding.title.text = item.name
            binding.description.text = item.description
        }
    }
}
