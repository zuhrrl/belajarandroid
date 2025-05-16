package com.sobodigital.zulbelajarandroid.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.sobodigital.zulbelajarandroid.data.model.EventItem

class EventDiffCallback(private val oldEventList: List<EventItem>, private val newEventList: List<EventItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldEventList.size
    override fun getNewListSize(): Int = newEventList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldEventList[oldItemPosition].id == newEventList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldEventList[oldItemPosition]
        val newNote = newEventList[newItemPosition]
        return oldNote.id == newNote.id
    }
}