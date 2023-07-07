package de.niilz.wearos.watchface.bttf

import android.graphics.Color
import androidx.core.graphics.ColorUtils

class DrawUtil {
    companion object {
        fun darkenColor(rgbColor: Int, ratio: Float): Int {
            return ColorUtils.blendARGB(rgbColor, Color.BLACK, ratio);
        }
    }
}