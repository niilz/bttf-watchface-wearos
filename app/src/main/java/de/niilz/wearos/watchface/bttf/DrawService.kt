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
    private val LABEL_SIZE = 16f

    fun drawSlots(
        slotData: List<SlotMetadata>,
        leftStart: Float,
        topStart: Float,
        gap: Float,
    ) {
        // FIRST-ROW
        var currentLeft = leftStart
        for (data in slotData) {
            val numbersToDraw = data.numbers?.let {
                MapperUtil.numbersToDrawables(
                    it,
                    numberBitmaps,
                    numberBitmaps[8],
                    colors.numberColorRow1,
                    colors.numberBackgroundColor
                )
            }
            val textToDraw = data.text?.let {
                listOf(DrawableText(it, 24f, colors.numberColorRow1))
            }
            val itemToDraw = numbersToDraw ?: textToDraw
            val label = Label(data.labelText, LABEL_SIZE)
            val drawableSlot = DrawableSlot(
                context,
                itemToDraw!!,
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