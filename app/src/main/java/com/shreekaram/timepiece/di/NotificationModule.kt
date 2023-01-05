package com.shreekaram.timepiece.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.shreekaram.timepiece.R
import com.shreekaram.timepiece.service.stopwatch.ServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

const val STOPWATCH_STATE = "STOPWATCH_STATE"

const val NOTIFICATION_CHANNEL_ID = "STOPWATCH_CHANNEL_ID"
const val NOTIFICATION_CHANNEL_NAME = "STOPWATCH_NOTIFICATION"
const val NOTIFICATION_ID = 10

const val CLICK_REQUEST_CODE = 100
const val CANCEL_REQUEST_CODE = 101
const val STOP_REQUEST_CODE = 102
const val RESUME_REQUEST_CODE = 103

enum class ActionService(value: String) {
    START("start"),
    STOP("stop"),
    CANCEL("cancel")
}

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {
    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Stopwatch")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setSilent(false)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "Stop", ServiceHelper.stopPendingIntent(context))
            .addAction(0, "Cancel", ServiceHelper.cancelPendingIntent(context))
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}
