package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas

class DrawService(
    private val context: Context,
    var numberBitmaps: List<Bitmap>,
    var canvas: Canvas?
) {
    // TODO: Do we really need both constructors?
    constructor(context: Context, numberBitmaps: List<Bitmap>) : this(context, numberBitmaps, null)

    companion object {
        private const val LABEL_SIZE = 16f
    }

    fun drawRow(
        leftStart: Float,
        top: Float,
        rowData: List<SlotMetadata>,
        footerText: String
    ) {
        var cursor = leftStart
        var bottomEnd = 0f
        for (slotData in rowData) {
            val (slotRightEnd, slotBottomEnd) = drawSlot(slotData, cursor, top)
            cursor += slotRightEnd + slotData.marginRight
            if (slotBottomEnd > bottomEnd) {
                bottomEnd = slotBottomEnd
            }
        }
        val footerLabel = Label(footerText, LABEL_SIZE)
        // TODO: Properly handle if canvas is not there
        val labelStart = (canvas!!.width / 2) - (footerLabel.getWidth() / 2)
        // TODO: Use a global and/or useful gap value
        // TODO: pass in a different Color
        canvas?.let {
            footerLabel.draw(
                it,
                labelStart,
                bottomEnd + context.resources.getDimension(R.dimen.gap) * 2
            )
        }
    }

    fun drawSlot(
        slotData: SlotMetadata,
        leftStart: Float,
        topStart: Float,
    ): Pair<Float, Float> {
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
        )
        canvas?.let {
            val (rightEnd, bottomEnd) = drawableSlot.draw(it)
            return Pair(rightEnd, bottomEnd)
        }
        throw IllegalStateException("Could not draw slot to canvas")
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

    private fun createBitmapSlot(slotMetadata: BitmapSlotMetadata): List<DrawableItem> {
        return slotMetadata.numbers.let {
            MapperUtil.numbersToDrawables(
                it,
                numberBitmaps,
                numberBitmaps[8],
                WatchFaceColors.NumberColorRow1,
                WatchFaceColors.NumberBackgroundColor
            )
        }
    }

    private fun createTextSlot(slotMetadata: TextSlotMetadata): List<DrawableItem> {
        return listOf(
            DrawableText(
                slotMetadata.text,
                getCharHeight(),
                getCharWidth(),
                WatchFaceColors.NumberColorRow1
            )
        )
    }

    private fun getCharWidth(): Float {
        return numberBitmaps[0].width.toFloat()
    }

    private fun getCharHeight(): Float {
        return numberBitmaps[1].height.toFloat()
    }
}