package com.shreekaram.timepiece.service.stopwatch

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.shreekaram.timepiece.MainActivity
import com.shreekaram.timepiece.di.*

enum class StopWatchIntent {
    START_SERVICE,
    PAUSE_SERVICE,
    CANCEL_SERVICE
}

enum class StopWatchCommand {
    START_NOTIFICATION,
    STOP_NOTIFICATION,
    START_SERVICE,
    PAUSE_SERVICE,
    CANCEL_SERVICE
}

object ServiceHelper {
    // 	since we are using api 26 we dont need check
    private val flag = PendingIntent.FLAG_IMMUTABLE

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, StopWatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchIntent.START_SERVICE.name)
        }

        return PendingIntent.getService(context, RESUME_REQUEST_CODE, resumeIntent, flag)
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StopWatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchIntent.PAUSE_SERVICE.name)
        }

        return PendingIntent.getService(context, STOP_REQUEST_CODE, stopIntent, flag)
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, StopWatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchIntent.CANCEL_SERVICE.name)
        }

        return PendingIntent.getService(context, CANCEL_REQUEST_CODE, cancelIntent, flag)
    }

    fun triggerForegroundService(context: Context, action: StopWatchCommand) {
        Intent(context, StopWatchService::class.java).apply {
            this.action = action.name
            context.startService(this)
        }
    }

    fun clickPendingIntent(context: Context): PendingIntent? {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchIntent.START_SERVICE.name)
        }

        return PendingIntent.getActivity(context, CLICK_REQUEST_CODE, clickIntent, flag)
    }
}
