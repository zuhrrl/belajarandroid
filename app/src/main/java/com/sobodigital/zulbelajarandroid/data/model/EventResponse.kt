package com.sobodigital.zulbelajarandroid.data.model

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<EventItem> = listOf(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
