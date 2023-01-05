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
import com.shreekaram.timepiece.di.*
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

    private val observer = Observer<Duration> { duration ->
        val message = formatDuration(duration)

        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(message).build()
        )
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            ActionService.START.name -> {
                setStopButton()
                startForegroundService()
                startStopWatch()
            }
            ActionService.STOP.name -> {
                stopStopWatch()
                setResumeButton()
            }
            ActionService.CANCEL.name -> {
                cancelStopwatch()
                stopForegroundService()
            }
        }

        intent?.action.let {
            when (it) {
                ActionService.START.name -> {
                    setStopButton()
                    startForegroundService()
                    startStopWatch()
                }
                ActionService.STOP.name -> {
                    stopStopWatch()
                    setResumeButton()
                }
                ActionService.CANCEL.name -> {
                    cancelStopwatch()
                    stopForegroundService()
                }
                else -> {}
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun cancelStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        stopWatchManager.updateDuration(Duration.ZERO)
        stopWatchManager.duration.removeObserver(observer)
        stopWatchManager.updateState(StopWatchState.IDLE)
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)

        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(0, "Stop", ServiceHelper.stopPendingIntent(this))
        )

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("SERVICE", "Received unbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @SuppressLint("RestrictedApi")
    fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)

        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(0, "Resume", ServiceHelper.resumePendingIntent(this))
        )

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startStopWatch() {
        stopWatchManager.updateState(StopWatchState.STARTED)

        stopWatchManager.duration.observeForever(observer)

        timer = Timer()

        scope.launch {
            time = SystemClock.elapsedRealtime()

            timer.schedule(
                timerTask {
                    val sysTime = SystemClock.elapsedRealtime()
                    val elapsed = sysTime - time

                    stopWatchManager.updateDuration(
//                        stopWatchManager.duration.value!!.plus(1.seconds)
                        stopWatchManager.duration.value!!.plus(elapsed.milliseconds)
                    )
                    time = sysTime
                },
                Date(),
                1000L
            )
        }

//        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
//            stopWatchManager.updateDuration(stopWatchManager.duration.value!!.plus(1.seconds))
//            Log.d("SERVICE", stopWatchManager.duration.value!!.inWholeSeconds.toString())
//        }
    }

    private fun stopStopWatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        stopWatchManager.duration.removeObserver(observer)

        stopWatchManager.updateState(StopWatchState.PAUSED)
    }

    private fun stopForegroundService() {
        Log.d("SERVICE", "Stopping service")
        notificationManager.cancel(NOTIFICATION_ID)
        stopWatchManager.duration.removeObserver(observer)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
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

    inner class StopWatchBinder : Binder() {
        fun getService(): StopWatchService = this@StopWatchService
    }
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun formatTime(seconds: String, minutes: String, hours: String): String {
    return "$hours:$minutes:$seconds"
}
