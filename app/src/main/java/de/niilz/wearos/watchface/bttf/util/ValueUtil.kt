package de.niilz.wearos.watchface.bttf.util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class ValueUtil {

    companion object {
        fun retrieveBatteryLevel(context: Context): Int? {
            val batteryStatus =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
                    context.registerReceiver(
                        null,
                        filter
                    )
                }
            val batteryPercent = batteryStatus?.let { intent ->
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                level * 100 / scale
            }
            return batteryPercent
        }
    }

}