package com.shreekaram.timepiece.domain.clock

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.Math.abs
import java.lang.reflect.Type
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue


data class TimeDuration(val hour: Long, val minutes: Long, val seconds: Long) {
	val zoneText:String
		get() {
			val minutesText = "${this.minutes.absoluteValue}".padStart(2, '0')
			val hourText = "${this.hour.absoluteValue}".padStart(2, '0')

			return "$hourText:$minutesText"
		}

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
			val _seconds =
				TimeUnit.SECONDS.toSeconds(seconds)

			return TimeDuration(hours, minutes=abs(minutes), seconds=_seconds)
		}
	}
}

fun TimeDuration.toGmtZone(): String {
	var prefix=if(this.hour>=0) "+" else "-"

	if( this.hour==0L && this.minutes<0 ){
		prefix="-"
	}

	return "${prefix} ${this.zoneText}"
}

fun TimeDuration.toZone(): String {
	var prefix=if(this.hour>=0) "+" else "-"

	if( this.hour==0L && this.minutes<0 ){
		prefix="-"
	}

	return "${prefix}${this.zoneText}"
}

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
){
	companion object{
		fun fromTimezone(timezone:TimeZone): NativeTimezone {
			val zoneName=timezone.getID()

			return	NativeTimezone(
				zoneName = zoneName,
				gmtOffset=timezone.rawOffset/1000F,
				abbreviation = "",
				isDst = timezone.dstSavings==1,
				cityName= zoneName.split("/").last(),
				duration = TimeDuration.fromSeconds(timezone.rawOffset/1000L)
			)
		}
	}
}


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
