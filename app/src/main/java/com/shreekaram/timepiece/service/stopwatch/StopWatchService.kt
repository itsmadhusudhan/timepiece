package com.shreekaram.timepiece.service.stopwatch

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.shreekaram.timepiece.di.NOTIFICATION_CHANNEL_ID
import com.shreekaram.timepiece.di.NOTIFICATION_CHANNEL_NAME
import com.shreekaram.timepiece.di.NOTIFICATION_ID
import com.shreekaram.timepiece.di.STOPWATCH_STATE
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

enum class StopWatchState {
    IDLE,
    STARTED,
    PAUSED,
}

fun logger(value: String) {
    Log.d("SERVICE", value)
}

val formatDuration: (Duration) -> String = {
    val message = it.toComponents { hours, minutes, seconds, _ ->
        return@toComponents formatTime(
            hours = hours.toInt().pad(),
            minutes = minutes.pad(),
            seconds = seconds.pad()
        )
    }

    message
}

@AndroidEntryPoint
class StopWatchService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var stopWatchManager: StopWatchManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StopWatchBinder()
    private lateinit var timer: Timer
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private var time = SystemClock.elapsedRealtime()

    private val durationObserver = Observer<Duration> { duration ->
        val message = formatDuration(duration)
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(message).build()
        )
    }

    private val stateObserver = Observer<StopWatchState> { state ->
        when (state) {
            StopWatchState.IDLE -> {
                notificationManager.cancel(NOTIFICATION_ID)
            }
            StopWatchState.STARTED -> {
                updateStopButton()
//                updateCancelButton()
            }
            StopWatchState.PAUSED -> {
                updateResumeButton()
            }
            else -> {
            }
        }
    }

    inner class StopWatchBinder : Binder() {
        fun getService(): StopWatchService = this@StopWatchService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        processIntentActions(intent?.getStringExtra(STOPWATCH_STATE))
        processCommandActions(intent?.action)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopWatchManager.stopWatchState.removeObserver(stateObserver)

        job.cancel()
    }

    private fun processIntentActions(action: String?) {
        when (action) {
            StopWatchIntent.START_SERVICE.name -> {
                startStopWatch()

                stopWatchManager.duration.observeForever(durationObserver)
            }
            StopWatchIntent.PAUSE_SERVICE.name -> {
                pauseStopWatch()
            }
            StopWatchIntent.CANCEL_SERVICE.name -> {
                cancelStopWatch()
                stopForegroundService()
                stopSelf()
            }
        }
    }

    private fun processCommandActions(action: String?) {
        when (action) {
            StopWatchCommand.START_SERVICE.name -> {
                startStopWatch()
            }
            StopWatchCommand.PAUSE_SERVICE.name -> {
                pauseStopWatch()
            }
            StopWatchCommand.CANCEL_SERVICE.name -> {
                cancelStopWatch()
                stopSelf()
            }
            StopWatchCommand.START_NOTIFICATION.name -> {
                startForegroundService()
            }
            StopWatchCommand.STOP_NOTIFICATION.name -> {
                stopForegroundService()
            }
        }
    }

    private fun startStopWatch() {
        stopWatchManager.updateState(StopWatchState.STARTED)

        scope.launch {
            timer = Timer()

            time = SystemClock.elapsedRealtime()

            timer.schedule(
                timerTask {
                    val sysTime = SystemClock.elapsedRealtime()
                    val elapsed = sysTime - time
                    val duration = stopWatchManager.duration.value!!.plus(elapsed.milliseconds)

                    stopWatchManager.updateDuration(duration)

                    time = sysTime
                },
                Date(),
                1000L
            )
        }
    }

    private fun pauseStopWatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        stopWatchManager.duration.removeObserver(durationObserver)
        stopWatchManager.updateState(StopWatchState.PAUSED)
    }

    private fun cancelStopWatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        stopWatchManager.duration.removeObserver(durationObserver)
        stopWatchManager.updateDuration(Duration.ZERO)
        stopWatchManager.updateState(StopWatchState.IDLE)
    }

    private fun startForegroundService() {
        if (stopWatchManager.stopWatchState.value === StopWatchState.STARTED) {
            createNotificationChannel()
            startForeground(NOTIFICATION_ID, notificationBuilder.build())
            stopWatchManager.stopWatchState.observeForever(stateObserver)
            stopWatchManager.duration.observeForever(durationObserver)
        }
    }

    private fun stopForegroundService() {
        if (stopWatchManager.stopWatchState.value === StopWatchState.STARTED) {
            notificationManager.cancel(NOTIFICATION_ID)
            stopWatchManager.stopWatchState.removeObserver(stateObserver)
            stopWatchManager.duration.removeObserver(durationObserver)
            stopForeground(STOP_FOREGROUND_REMOVE)
//            stopSelf()
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        channel.setSound(null, null)
        channel.setShowBadge(false)
        channel.enableVibration(false)

        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("RestrictedApi")
    private fun updateStopButton() {
        if (notificationBuilder.mActions.isNotEmpty()) {
            notificationBuilder.mActions.clear()
        }

        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(0, "Lap", ServiceHelper.stopPendingIntent(this))
        )
        notificationBuilder.mActions.add(
            1,
            NotificationCompat.Action(0, "Stop", ServiceHelper.stopPendingIntent(this))
        )

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun updateResumeButton() {
        if (notificationBuilder.mActions.isNotEmpty()) {
            notificationBuilder.mActions.clear()
        }

        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(0, "Reset", ServiceHelper.cancelPendingIntent(this))
        )
        notificationBuilder.mActions.add(
            1,
            NotificationCompat.Action(0, "Start", ServiceHelper.resumePendingIntent(this))
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun formatTime(seconds: String, minutes: String, hours: String): String {
    return "$hours:$minutes:$seconds"
}
