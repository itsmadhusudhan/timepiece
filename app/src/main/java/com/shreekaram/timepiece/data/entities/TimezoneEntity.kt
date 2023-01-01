package com.shreekaram.timepiece.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.TimeDuration
import com.shreekaram.timepiece.domain.clock.parseCityName

@Entity(tableName = "timezone", indices = [Index(value = ["zone_name"])],)
data class TimezoneEntity(
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "zone_name")
	val zoneName: String,
	@ColumnInfo(name = "gmt_offset")
	val gmtOffset: Float,
	@ColumnInfo(name = "is_dst")
	val isDst: Boolean,
	@ColumnInfo(name = "abbreviation")
	val abbreviation: String
){
	companion object{
		fun fromDomain(timezone: NativeTimezone): TimezoneEntity{
			return TimezoneEntity(
				zoneName = timezone.zoneName,
				gmtOffset = timezone.gmtOffset,
				isDst = timezone.isDst,
				abbreviation = timezone.abbreviation
			)
		}
	}
}

fun TimezoneEntity.toDomain(): NativeTimezone{
	return NativeTimezone(
		zoneName= zoneName,
		gmtOffset= gmtOffset,
		isDst = isDst,
		abbreviation = abbreviation,
		cityName= parseCityName(zoneName),
		duration = TimeDuration.fromSeconds(gmtOffset.toLong())
	)
}
