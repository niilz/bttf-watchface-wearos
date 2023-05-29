package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Canvas

class DrawService(private val canvas: Canvas, private val context: Context) {

    fun drawDrawables(
        slotData: List<List<DrawableItem>>,
        leftStart: Float,
        topStart: Float,
        gap: Float
    ) {
        var currentLeft = leftStart
        for (data in slotData) {
            val slot = Slot(
                context,
                data,
                currentLeft,
                topStart,
                gap
            )
            slot.draw(canvas)
            currentLeft += slot.getWidth() + (3 * gap)
        }
    }
}