package com.app.penyakitdaunjagung.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("current")
	val current: Current,

	@field:SerializedName("location")
	val location: Location
)

data class Current(

	@field:SerializedName("feelslike_c")
	val feelslikeC: Double? = null,

	@field:SerializedName("uv")
	val uv: Double? = null,

	@field:SerializedName("condition")
	val condition: Condition,

	@field:SerializedName("feelslike_f")
	val feelslikeF: Double? = null,

	@field:SerializedName("temp_c")
	val tempC: Double? = null,

	@field:SerializedName("temp_f")
	val tempF: Double? = null
)

data class Condition(

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)

data class Location(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("region")
	val region: String? = null
)
