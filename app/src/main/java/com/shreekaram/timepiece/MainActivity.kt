package com.shreekaram.timepiece

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shreekaram.timepiece.presentation.clock.ClockStateViewModel
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel
import com.shreekaram.timepiece.presentation.clock.UTCTimeModelView
import com.shreekaram.timepiece.presentation.home.RootNavigationGraph
import com.shreekaram.timepiece.service.stopwatch.ServiceHelper
import com.shreekaram.timepiece.service.stopwatch.StopWatchCommand
import com.shreekaram.timepiece.service.stopwatch.StopWatchService
import com.shreekaram.timepiece.ui.theme.TimePieceTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalTimezoneViewModel = compositionLocalOf<TimezoneViewModel> {
    error("Timezones are not set")
}
val LocalUTCTimeViewModel = compositionLocalOf<UTCTimeModelView> {
    error("utc date not set")
}
val LocalClockStateViewModel = compositionLocalOf<ClockStateViewModel> {
    error("clock state is not set")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val utcViewModel: UTCTimeModelView by viewModels()
    private lateinit var stopWatchService: StopWatchService
    private var isBounded by mutableStateOf(false)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as StopWatchService.StopWatchBinder
            stopWatchService = binder.getService()
            isBounded = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("SERVICE", "Service disconnected")

            isBounded = false
        }
    }

    override fun onStart() {
        Log.d("SERVICE", "started activity")
        Intent(this, StopWatchService::class.java).also {
            Log.d("SERVICE", "bounded service")

            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        super.onStart()
    }

    override fun onStop() {
        if (isBounded) {
            Log.d("SERVICE", "unbinded service")
            unbindService(connection)
            isBounded = false
        }

        super.onStop()
    }

    override fun onPause() {
        Log.d("SERVICE", "paused activity $isBounded")
        ServiceHelper.triggerForegroundService(
            this@MainActivity,
            StopWatchCommand.START_NOTIFICATION
        )
        super.onPause()
    }

    override fun onResume() {
        Log.d("SERVICE", "resumed activity")
        ServiceHelper.triggerForegroundService(
            this@MainActivity,
            StopWatchCommand.STOP_NOTIFICATION
        )
        super.onResume()
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                result.entries.forEach {
                    Log.d("MainActivity", "${it.key} = ${it.value}")
                }
            }

        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            TimePieceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val timezoneViewModel = hiltViewModel<TimezoneViewModel>()
                    val clockStateViewModel = hiltViewModel<ClockStateViewModel>()

                    CompositionLocalProvider(
                        LocalTimezoneViewModel provides timezoneViewModel,
                        // FIXME: refactor to localise it
                        LocalUTCTimeViewModel provides utcViewModel,
                        LocalClockStateViewModel provides clockStateViewModel
                    ) {
                        val navController = rememberAnimatedNavController()

                        RootNavigationGraph(navController = navController)
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

//    NOTE: this is just for documentation
//    override fun onPause() {
//        super.onPause()
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
//    }
//
//    override fun onResume() {
//        super.onResume()
//        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
//    }
}
