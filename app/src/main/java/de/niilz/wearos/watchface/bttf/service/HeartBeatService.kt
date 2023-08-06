package de.niilz.wearos.watchface.bttf.service

import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType

class HeartBeatService : PassiveListenerService() {

    val healthServiceClient = HealthServices.getClient(applicationContext)

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
            Log.i("listener", "$it")
        }
    }
}