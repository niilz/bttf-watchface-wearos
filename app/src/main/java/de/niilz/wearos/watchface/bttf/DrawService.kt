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

    companion object {
        private const val LABEL_SIZE = 16f
    }

    fun drawSlots(
        slotData: List<SlotMetadata>,
        leftStart: Float,
        topStart: Float,
        gap: Float,
    ) {
        // FIRST-ROW
        var currentLeft = leftStart
        for (data in slotData) {
            // TODO: Be smarter with the handling of "is it a number or Text"
            val numbersToDraw = data.numbers?.let {
                MapperUtil.numbersToDrawables(
                    it,
                    numberBitmaps,
                    numberBitmaps[8],
                    colors.numberColorRow1,
                    colors.numberBackgroundColor
                )
            }
            // TODO: Be smarter with the handling of "is it a number or Text"
            val textToDraw = data.text?.let {
                listOf(DrawableText(it, 24f, getCharWidth(), colors.numberColorRow1))
            }
            // TODO: Be smarter with the handling of "is it a number or Text"
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
        initialNumberWidth: Float,
        maxNumbersPerRow: Int,
        maxNumbersPerCol: Int
    ) {
        val targetWidthFractionOfCanvas = 1f / maxNumbersPerRow
        val widthScalar = MapperUtil.percentOfInnerCanvasSkalar(
            canvasInnerWidthOrHeight,
            initialNumberWidth,
            targetWidthFractionOfCanvas
        )
        val targetHeightFractionOfCanvas = 1f / maxNumbersPerCol
        val heightScalar = MapperUtil.percentOfInnerCanvasSkalar(
            canvasInnerWidthOrHeight,
            initialNumberHeight,
            targetHeightFractionOfCanvas
        )
        numberBitmaps =
            numberBitmaps.map {
                MapperUtil.scaleBitmap(
                    it,
                    widthScale = widthScalar,
                    heightScale = heightScalar
                )
            }
    }

    private fun getCharWidth(): Float {
        return numberBitmaps[0].width.toFloat()
    }
}