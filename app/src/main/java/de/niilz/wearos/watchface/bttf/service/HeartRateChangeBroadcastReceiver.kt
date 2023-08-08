package de.niilz.wearos.watchface.bttf.service

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HeartRateChangeBroadcastReceiver() : BroadcastReceiver() {

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onReceive(context: Context, intent: Intent) {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val extras = intent.extras ?: return
    val dataSource = extras.getParcelable<ComponentName>(EXTRA_HEART_BEAT_DATASOURCE_COMPONENT, ComponentName::class.java) ?: return
    val complicationId = extras.getInt(EXTRA_HEART_BEAT_DATASOURCE_ID)

    val result = goAsync()

    scope.launch {
      try {
        context.dataStore.edit { preferences ->
          val currentValue = preferences[HEART_RATE_PREF_KEY] ?: 0
          // FIXME: Do we do anything with the HEART Rate here? (it is not like the counter example)
        }
        val complicationDataSourceUpdateRequester = ComplicationDataSourceUpdateRequester.create(context, dataSource)
        complicationDataSourceUpdateRequester.requestUpdate(complicationId)
      } finally {
        result.finish()
      }
    }

  }

  companion object {
    private const val EXTRA_HEART_BEAT_DATASOURCE_COMPONENT = "de.niilz.wearos.watchface.bttf.heartbeat.DATASOURCE_COMPONENT"
    private const val EXTRA_HEART_BEAT_DATASOURCE_ID = "de.niilz.wearos.watchface.bttf.heartbeat.COMPLICATION_ID"
  }

}
