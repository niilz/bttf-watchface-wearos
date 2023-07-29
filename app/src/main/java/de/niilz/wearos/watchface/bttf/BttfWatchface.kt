package de.niilz.wearos.watchface.bttf

import android.graphics.RectF
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema

class BttfWatchface : WatchFaceService() {
    override fun createUserStyleSchema(): UserStyleSchema {
        return UserStyleSchema(listOf())
    }

    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager {
        val defaultCompicationDrawable = ComplicationDrawable(applicationContext)
        val defaultComplicationFactory =
            CanvasComplicationFactory { watchState, listener ->
                CanvasComplicationDrawable(defaultCompicationDrawable, watchState, listener)
            }
        // TODO: map from 0 to 4 and set a default value
        val complicationSlots = (0..4).map { i ->
            val complicationDrawable = ComplicationDrawable(applicationContext)
            complicationDrawable.noDataText = "Ooops"
            ComplicationSlot.createRoundRectComplicationSlotBuilder(
                id = 42 + i,
                canvasComplicationFactory = defaultComplicationFactory,
                supportedTypes = listOf(ComplicationType.SHORT_TEXT),
                defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                    SystemDataSources.DATA_SOURCE_WATCH_BATTERY,
                    ComplicationType.SHORT_TEXT,
                ),
                bounds = ComplicationSlotBounds(RectF(0f, 0f, 0f, 0f))
            ).build()
        }
        return ComplicationSlotsManager(complicationSlots, currentUserStyleRepository)
    }

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