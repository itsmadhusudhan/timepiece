package com.shreekaram.timepiece.domain.clock

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.shreekaram.timepiece.proto.TimezoneModel
import java.lang.reflect.Type
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlinx.serialization.Serializable

@Serializable
data class TimeDuration(val hour: Long, val minutes: Long, val seconds: Long) {
    val zoneText: String
        get() {
            val minutesText = "${this.minutes.absoluteValue}".padStart(2, '0')
            val hourText = "${this.hour.absoluteValue}".padStart(2, '0')

            return "$hourText:$minutesText"
        }

    companion object Convert {
        fun fromMilliseconds(milliseconds: Long): TimeDuration {
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            val minutes = TimeUnit.MILLISECONDS
                .toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1)
            val seconds = TimeUnit.MILLISECONDS
                .toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)

            return TimeDuration(hours, minutes = abs(minutes), seconds)
        }

        fun fromSeconds(seconds: Long): TimeDuration {
            val hours = TimeUnit.SECONDS.toHours(seconds)
            val minutes = TimeUnit.SECONDS.toMinutes(seconds) % TimeUnit.HOURS.toMinutes(1)
            val _seconds = TimeUnit.SECONDS.toSeconds(seconds)

            return TimeDuration(hours, minutes = abs(minutes), seconds = _seconds)
        }
    }
}

fun TimeDuration.toGmtZone(): String {
    var prefix = if (this.hour >= 0) "+" else "-"

    if (this.hour == 0L && this.minutes <0) {
        prefix = "-"
    }

    return "$prefix ${this.zoneText}"
}

fun TimeDuration.toZone(): String {
    var prefix = if (this.hour >= 0) "+" else "-"

    if (this.hour == 0L && this.minutes <0) {
        prefix = "-"
    }

    return "${prefix}${this.zoneText}"
}

@Serializable
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
) {
    companion object {
        fun current(): NativeTimezone {
            val timezone = TimeZone.getDefault()

            val zoneName = timezone.id

            return	NativeTimezone(
                zoneName = zoneName,
                gmtOffset = timezone.rawOffset / 1000F,
                abbreviation = "",
                isDst = timezone.dstSavings == 1,
                cityName = parseCityName(zoneName),
                duration = TimeDuration.fromSeconds(timezone.rawOffset / 1000L)
            )
        }

        fun fromTimezoneModel(timezone: TimezoneModel): NativeTimezone {
            val zoneName = timezone.zoneName

            return	NativeTimezone(
                zoneName = zoneName,
                gmtOffset = timezone.gmtOffset,
                abbreviation = timezone.abbreviation,
                isDst = timezone.isDst,
                cityName = parseCityName(zoneName),
                duration = TimeDuration.fromSeconds(timezone.gmtOffset.toLong())
            )
        }
    }
}

fun parseCityName(name: String): String {
    return name.split("/").last().split("_").joinToString(" ")
}

fun NativeTimezone.toTimezoneModel(): TimezoneModel {
    return TimezoneModel.newBuilder()
        .setZoneName(this.zoneName)
        .setAbbreviation(this.abbreviation)
        .setIsDst(this.isDst)
        .setGmtOffset(this.gmtOffset)
        .build()
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
            isDst = dst == "1"
        )
    }
}
