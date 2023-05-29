package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Canvas

class DrawService(val canvas: Canvas, context: Context) {

    fun drawDrawables(
        context: Context,
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