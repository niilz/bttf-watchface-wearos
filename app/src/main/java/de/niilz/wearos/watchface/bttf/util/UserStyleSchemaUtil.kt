package de.niilz.wearos.watchface.bttf.util

import android.content.Context
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import de.niilz.wearos.watchface.bttf.R

// FIXME: We probably need to add a ConfigStateHolder aswell, use it in the editor and listen for updates
fun createBttfComplicationUserStyleSchema(applicationContext: Context): UserStyleSchema {
  val optionsConfig = (0..7).map { createComplicationSlotOptions(it) }
  val complicationSettings = UserStyleSetting.ComplicationSlotsUserStyleSetting(
    id = UserStyleSetting.Id("complication-slot-settings"),
    resources = applicationContext.resources,
    icon = null,
    displayNameResourceId = R.string.SLOT_COUNT_SETTING,
    descriptionResourceId = R.string.SLOT_COUNT_SETTING,
    complicationConfig = optionsConfig,
    affectsWatchFaceLayers = listOf(WatchFaceLayer.COMPLICATIONS),
  )
  return UserStyleSchema(listOf(complicationSettings))

}

private fun createComplicationSlotOptions(slotCounts: Int): UserStyleSetting.ComplicationSlotsUserStyleSetting.ComplicationSlotsOption {
  val complicationOverlays = (1..slotCounts).map { slotId ->
    createComplicationSlotOverlay(slotId)
  }
  return UserStyleSetting.ComplicationSlotsUserStyleSetting.ComplicationSlotsOption(
    UserStyleSetting.Option.Id("slot-counts-${complicationOverlays.size}"),
    "Slot-Counts-${complicationOverlays.size}",
    null,
    complicationOverlays
  )
}

private fun createComplicationSlotOverlay(id: Int): UserStyleSetting.ComplicationSlotsUserStyleSetting.ComplicationSlotOverlay {
  // Log.d(TAG, "Slot-Overlay-ID: $id")
  return UserStyleSetting.ComplicationSlotsUserStyleSetting.ComplicationSlotOverlay(
    // Starts with id = 1 -> 101
    complicationSlotId = 100 + id,
    enabled = true,
  )
}