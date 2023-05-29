package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas

class DrawService(
    private val context: Context,
    var numberBitmaps: List<Bitmap>,
    var canvas: Canvas?
) {
    constructor(context: Context, numberBitmaps: List<Bitmap>) : this(context, numberBitmaps, null)

    fun drawNumberSlot(
        slotData: List<List<Int>>,
        leftStart: Float,
        topStart: Float,
        gap: Float,
        color: Int
    ) {
        var currentLeft = leftStart
        for (data in slotData) {
            val numbersToDraw = MapperUtil.numbersToDrawables(data, numberBitmaps, color)
            val slot = Slot(
                context,
                numbersToDraw,
                currentLeft,
                topStart,
                gap
            )
            canvas?.let {
                slot.draw(it)
            }
            currentLeft += slot.getWidth() + (3 * gap)
        }
    }

    fun updateNumbers(
        canvasInnerWidthOrHeight: Float,
        initialNumberHeight: Float,
        targetPercentOfCanvas: Int
    ) {
        val numberSkalar = MapperUtil.percentOfInnerCanvasSkalar(
            canvasInnerWidthOrHeight,
            initialNumberHeight,
            targetPercentOfCanvas
        )
        numberBitmaps = numberBitmaps.map { MapperUtil.scaleBitmap(it, numberSkalar) }
    }
}