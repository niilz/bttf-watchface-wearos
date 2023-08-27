package de.niilz.wearos.watchface.bttf.util

import android.content.Context
import android.graphics.RectF
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository

const val MAX_SLOT_COUNT = 6
fun createComplicationSlotManager(
  applicationContext: Context,
  userStyleRepository: CurrentUserStyleRepository
): ComplicationSlotsManager {

  val defaultCompicationDrawable = ComplicationDrawable(applicationContext)
  val defaultComplicationFactory =
    CanvasComplicationFactory { watchState, listener ->
      CanvasComplicationDrawable(defaultCompicationDrawable, watchState, listener)
    }
  val complicationSlots = (0..MAX_SLOT_COUNT).map { i ->
    val complicationDrawable = ComplicationDrawable(applicationContext)
    complicationDrawable.noDataText = "Ooops"
    ComplicationSlot.createRoundRectComplicationSlotBuilder(
      id = i,
      canvasComplicationFactory = defaultComplicationFactory,
      supportedTypes = listOf(ComplicationType.SHORT_TEXT),
      defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
        SystemDataSources.NO_DATA_SOURCE,
        ComplicationType.SHORT_TEXT,
      ),
      bounds = ComplicationSlotBounds(RectF(0f, 0f, 0f, 0f))
    ).build()
  }
  return ComplicationSlotsManager(complicationSlots, userStyleRepository)
}
