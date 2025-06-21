package com.sobodigital.zulbelajarandroid.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.databinding.ItemStoryBinding

class StoryAdapterWithPaging() :
    PagingDataAdapter<Story, StoryAdapterWithPaging.StoriesViewHolder>(DIFF_CALLBACK) {
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }


    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    class StoriesViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story){
            Glide.with(binding.root).load(item.photoUrl).into(binding.imgEvent)
            binding.title.text = item.name
            binding.description.text = item.description
        }
    }
}
