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

    fun drawSlot(
        slotData: SlotMetadata,
        leftStart: Float,
        topStart: Float,
    ): Float {
        var currentLeft = leftStart
        val itemToDraw = when (slotData) {
            is BitmapSlotMetadata -> createBitmapSlot(slotData)
            is TextSlotMetadata -> createTextSlot(slotData)
            else -> throw UnsupportedOperationException("Slot-Type not supported")
        }
        val label = Label(slotData.labelText, LABEL_SIZE)
        val drawableSlot = DrawableSlot(
            context,
            itemToDraw,
            label,
            currentLeft,
            topStart,
            slotData.marginRight
        )
        canvas?.let {
            drawableSlot.draw(it)
        }
        // TODO: marginRight is what again? the background or the gap to the next slot?...
        return drawableSlot.getWidth() + (3 * slotData.marginRight)
    }

    private fun createBitmapSlot(slotMetadata: BitmapSlotMetadata): List<DrawableItem> {
        return slotMetadata.numbers.let {
            MapperUtil.numbersToDrawables(
                it,
                numberBitmaps,
                numberBitmaps[8],
                colors.numberColorRow1,
                colors.numberBackgroundColor
            )
        }
    }

    private fun createTextSlot(slotMetadata: TextSlotMetadata): List<DrawableItem> {
        return slotMetadata.text.let {
            // TODO: Actually caclulate how high the text should be
            listOf(DrawableText(it, 30f, getCharWidth(), colors.numberColorRow1))
        }
    }

    fun updateNumbers(
        canvasInnerWidthOrHeight: Float,
        initialNumberWidth: Float,
        initialNumberHeight: Float,
        maxNumbersPerRow: Int,
        maxNumbersPerCol: Int
    ) {
        val targetNumberWidth = canvasInnerWidthOrHeight / maxNumbersPerRow
        val widthScalar = targetNumberWidth / initialNumberWidth

        val targetNumberHeight = canvasInnerWidthOrHeight / maxNumbersPerCol
        val heightScalar = targetNumberHeight / initialNumberHeight

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