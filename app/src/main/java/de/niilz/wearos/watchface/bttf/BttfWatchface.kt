package de.niilz.wearos.watchface.bttf

import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import de.niilz.wearos.watchface.bttf.service.HeartBeatService
import de.niilz.wearos.watchface.bttf.util.createComplicationSlotManager
import kotlinx.coroutines.runBlocking

class BttfWatchface : WatchFaceService() {

    private lateinit var heartBeatService: HeartBeatService

    override fun createUserStyleSchema(): UserStyleSchema {
        return UserStyleSchema(listOf())
    }

    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager {
        return createComplicationSlotManager(applicationContext, currentUserStyleRepository)
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {

        heartBeatService = HeartBeatService(applicationContext)
        Log.i(TAG, "Registering HeartBeatReceiver")

        val renderer = WatchFaceRenderer(
            applicationContext,
            resources,
            surfaceHolder,
            watchState,
            complicationSlotsManager,
            currentUserStyleRepository,
            CanvasType.HARDWARE
        )
        Log.i(TAG, "Creating BTTF-Watchface")
        return WatchFace(WatchFaceType.DIGITAL, renderer)
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            Log.i(TAG, "Unregistering HeartBeatReceiver")
            heartBeatService.unregisterHeartBeatReceive()
        }
    }
}

const val TAG = "### Bttf-Watchface"
