package de.niilz.wearos.watchface.bttf

import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema

class BttfWatchface : WatchFaceService() {
    override fun createUserStyleSchema(): UserStyleSchema {
        return UserStyleSchema(listOf())
    }

    // FIXME: This is wothless but it does draw a circle complication on the right
    /*
    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager {
        val complicationDrawable = ComplicationDrawable(applicationContext)
        complicationDrawable.noDataText = "Ooops"
        val defaultComplicationFactory =
            CanvasComplicationFactory { watchState, listener ->
                CanvasComplicationDrawable(complicationDrawable, watchState, listener)
            }
        val batterySlot = ComplicationSlot.createRoundRectComplicationSlotBuilder(
            id = 42,
            canvasComplicationFactory = defaultComplicationFactory,
            supportedTypes = listOf(ComplicationType.SHORT_TEXT),
            defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_WATCH_BATTERY,
                ComplicationType.SHORT_TEXT,
            ),
            bounds = ComplicationSlotBounds(RectF(0.6f, 0.4f, 0.8f, 0.6f))
        ).build()
        return ComplicationSlotsManager(listOf(batterySlot), currentUserStyleRepository)
    }
     */

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {

        val renderer = WatchFaceRenderer(
            applicationContext,
            resources,
            surfaceHolder,
            watchState,
            complicationSlotsManager,
            currentUserStyleRepository,
            CanvasType.HARDWARE
        )
        return WatchFace(WatchFaceType.DIGITAL, renderer)
    }

}