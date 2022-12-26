package com.shreekaram.timepiece.domain.clock

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.Math.abs
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

data class Zone(val offset: Float, val abbreviation: String, val isDst: Boolean)

data class TimeZone(
	val name: String,
	val currentTimeZone: Zone,
	val transitionZone: List<Int>,
	val zones: List<Zone>,
	var cityName: String = "",
	var duration: TimeDuration = TimeDuration(0, 0, 0)
)

data class TimeDuration(val hour: Long, val minutes: Long, val seconds: Long) {
	companion object Convert {
		fun fromMilliseconds(milliseconds: Long): TimeDuration {
			val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
			val minutes =
				TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1)
			val seconds =
				TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)

			return TimeDuration(hours, minutes=abs(minutes), seconds)
		}

		fun fromSeconds(seconds: Long): TimeDuration {
			val hours = TimeUnit.SECONDS.toHours(seconds)
			val minutes =
				TimeUnit.SECONDS.toMinutes(seconds) % TimeUnit.HOURS.toMinutes(1)
			val seconds =
				TimeUnit.SECONDS.toSeconds(seconds)

			return TimeDuration(hours, minutes=abs(minutes), seconds)
		}
	}
}

fun TimeDuration.toZone(): String {
	val minutesText = "${this.minutes}".padStart(2, '0')

	return "${this.hour}:$minutesText"
}


//data class NativeTimezone(
//	val name: String,
//	@SerializedName("currentTimeZone.offset")
//	val offset: Float,
//	@SerializedName("currentTimeZone.abbreviation")
//	val abbreviation: String,
//	@SerializedName("currentTimeZone.isDst")
//	val isDst: Boolean,
//	var cityName: String = "",
//	var duration: TimeDuration = TimeDuration(0, 0, 0)
//)

//class NativeTimezoneDeserializer : JsonDeserializer<NativeTimezone> {
//	override fun deserialize(
//		json: JsonElement?,
//		typeOfT: Type?,
//		context: JsonDeserializationContext?
//	): NativeTimezone {
//		val jsonObject = json?.asJsonObject!!
//
//		val name = jsonObject.get("name").asString
//
//		val zone =
//			context?.deserialize<Zone>(jsonObject.get("currentTimeZone"), Zone::class.java)!!;
//
//		return NativeTimezone(name, zone.offset, zone.abbreviation, zone.isDst)
//	}
//
//}
//

data class NativeTimezone(
	@SerializedName("zone_name")
	val zoneName: String,
	@SerializedName("gmt_offset")
	val gmtOffset: Float,
	val abbreviation: String,
	@SerializedName("dst")
	val isDst: Boolean,
	var cityName: String = "",
	var duration: TimeDuration = TimeDuration(0, 0, 0)
)


class NativeTimezoneDeserializer : JsonDeserializer<NativeTimezone> {
	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): NativeTimezone {
		val jsonObject = json?.asJsonObject!!

		val zoneName = jsonObject.get("zone_name").asString
		val gmtOffset = jsonObject.get("gmt_offset").asFloat
		val abbreviation = jsonObject.get("abbreviation").asString
		val dst = jsonObject.get("zone_name").asString

		return NativeTimezone(
			zoneName,
			gmtOffset,
			abbreviation,
			isDst=if(dst=="1") true else false
		)
	}

}
