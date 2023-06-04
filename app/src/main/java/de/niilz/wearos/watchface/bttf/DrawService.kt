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

    private val colors = NumberColors(context)

    fun drawNumberSlot(
        slotData: List<List<Int>>,
        leftStart: Float,
        topStart: Float,
        gap: Float,
    ) {
        // FIRST-ROW
        var currentLeft = leftStart
        for (data in slotData) {
            val label = Label("Foo", 17f)
            val numbersToDraw = MapperUtil.numbersToDrawables(
                data,
                numberBitmaps,
                numberBitmaps[8],
                colors.numberColorRow1,
                colors.numberBackgroundColor
            )
            val drawableSlot = DrawableSlot(
                context,
                numbersToDraw,
                label,
                currentLeft,
                topStart,
                gap
            )
            canvas?.let {
                drawableSlot.draw(it)
            }
            currentLeft += drawableSlot.getWidth() + (3 * gap)
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