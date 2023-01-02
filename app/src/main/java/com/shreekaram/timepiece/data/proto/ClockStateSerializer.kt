package com.shreekaram.timepiece.data.proto

import androidx.datastore.core.Serializer
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.toTimezoneModel
import com.shreekaram.timepiece.proto.ClockState
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ClockStateSerializer : Serializer<ClockState> {
    override val defaultValue: ClockState
        get() = ClockState.getDefaultInstance().toBuilder()
            .setHomeTimezone(
                NativeTimezone.current()
                    .toTimezoneModel()
            ).build()

    override suspend fun readFrom(input: InputStream): ClockState = withContext(Dispatchers.IO) {
        try {
            return@withContext ClockState.parseFrom(input)
        } catch (e: Exception) {
            e.printStackTrace()

            return@withContext defaultValue
        }
    }

    override suspend fun writeTo(
        t: ClockState,
        output: OutputStream
    ) = withContext(
        Dispatchers.IO
    ) {
        t.writeTo(output)
    }
}
