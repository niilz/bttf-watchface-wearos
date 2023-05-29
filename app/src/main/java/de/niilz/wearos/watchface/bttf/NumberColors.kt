package de.niilz.wearos.watchface.bttf

import android.content.Context
import androidx.core.content.ContextCompat

class NumberColors(context: Context) {

    val numberColorRow1 =
        ContextCompat.getColor(context, R.color.number_color_row_1)
    val numberColorRow2 =
        ContextCompat.getColor(context, R.color.number_color_row_2)
    val numberColorRow3 =
        ContextCompat.getColor(context, R.color.number_color_row_3)
    val numberBackgroundColor =
        ContextCompat.getColor(context, R.color.number_background_color)
    val watchNumberShadow =
        ContextCompat.getColor(context, R.color.watch_shadow_color)
}