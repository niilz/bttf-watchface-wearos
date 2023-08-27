package de.niilz.wearos.watchface.bttf.service

import android.util.Log
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import de.niilz.wearos.watchface.bttf.TAG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class HeartRateComplicationDSService : SuspendingComplicationDataSourceService() {
  override fun getPreviewData(type: ComplicationType): ComplicationData? {
    Log.w(TAG, "TODO: Render Preview Here")
    return null
  }

  override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {

    val heartBeat: Int = applicationContext.dataStore.data.map { preferences ->
      preferences[HEART_RATE_PREF_KEY] ?: 0
    }.first()

    //Log.d(TAG, "Collected hearbeat from datastore: $heartBeat")
    val heartBeatText = "$heartBeat"

    return when (request.complicationType) {
      ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(
        text = PlainComplicationText.Builder(heartBeatText).build(),
        contentDescription = PlainComplicationText.Builder(text = "Current Heart Rate in BPM")
          .build(),
      ).build()

      else -> {
        if (Log.isLoggable(TAG, Log.WARN)) {
          Log.w(TAG, "Unexpected complication type")
        }
        null
      }

    }

  }
}
