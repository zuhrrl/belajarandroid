package com.sobodigital.zulbelajarandroid.data.model

import com.google.gson.annotations.SerializedName

data class EventDetailResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("event")
	val event: EventItem? = null
)

