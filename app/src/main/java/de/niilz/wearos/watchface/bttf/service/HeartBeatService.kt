package de.niilz.wearos.watchface.bttf.service

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.clearPassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import de.niilz.wearos.watchface.bttf.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeartBeatService(context: Context) {

    private val healthClient = HealthServices.getClient(context)
    private val passiveMonitoringClient = healthClient.passiveMonitoringClient
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    init {
        val dataConfig = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .build()
        val listenerCallback = object : PassiveListenerCallback {
            override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
                val heartRates = dataPoints.getData(DataType.HEART_RATE_BPM)
                    for (heartRate in heartRates) {
                        //Log.i(TAG, "HeartRate: ${preferences[HEART_RATE_PREF_KEY]}")
                        Log.i(TAG, "HeartRate: ${heartRate.value}")
                        /*
                        context.dataStore.edit { preferences ->
                            preferences[HEART_RATE_PREF_KEY] = heartRate.value.toInt()
                        }
                         */
                    }
                }
            }
        passiveMonitoringClient.setPassiveListenerCallback(dataConfig, listenerCallback)
    }

    suspend fun hasHeartBeatCapabilities(): Boolean {
        val capabilities = passiveMonitoringClient.getCapabilities()
        return DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
    }

    suspend fun unregisterHeartBeatReceive() {
        Log.i(TAG, "Unregister HeartBeatListener")
        passiveMonitoringClient.clearPassiveListenerCallback()
    }
}
