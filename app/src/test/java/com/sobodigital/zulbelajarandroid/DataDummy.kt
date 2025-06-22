package com.sobodigital.zulbelajarandroid

import com.sobodigital.zulbelajarandroid.domain.model.Story
import java.io.File


object DataDummy {

    fun generateDummyStories(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = "id_$i",
                photoUrl = "photo_url_$i",
                name = "name_$i",
                description = "description_$i",
                lat = 1.0,
                lon = 1.0
            )
            items.add(story)
        }
        return items
    }

    fun getDummyStory(): Story {
        return  Story(
            id = "id_test",
            photoUrl = "photo_url",
            name = "name_",
            description = "description",
            lat = 1.0,
            lon = 1.0
        )
    }

    fun generateSampleImage(): File {
        val file = File.createTempFile("image_sample", ".jpg")
        val imageBytes = ByteArray(900) { 0 }
        file.writeBytes(imageBytes)
        return file
    }

}