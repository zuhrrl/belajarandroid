package com.sobodigital.zulbelajarandroid

import com.sobodigital.zulbelajarandroid.data.model.Story

object DataDummy {

    fun generateDummyStories(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val quote = Story(
                id = "id_$i",
                photoUrl = "photo_url_$i",
                name = "name_$i",
                description = "description_$i",
            )
            items.add(quote)
        }
        return items
    }
}