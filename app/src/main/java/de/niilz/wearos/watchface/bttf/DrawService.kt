package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import de.niilz.wearos.watchface.bttf.config.WatchFaceColors
import de.niilz.wearos.watchface.bttf.drawable.DrawableItem
import de.niilz.wearos.watchface.bttf.drawable.DrawableSlot
import de.niilz.wearos.watchface.bttf.drawable.DrawableText

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
        footerText: String,
        backgroundColor: Int
    ): Float {
        var cursor = leftStart
        var slotBottom = 0f
        for (slotData in rowData) {
            val (slotRightEnd, slotBottomEnd) = drawSlot(slotData, cursor, top, backgroundColor)
            cursor += slotRightEnd + slotData.marginRight
            if (slotBottomEnd > slotBottom) {
                slotBottom = slotBottomEnd
            }
        }
        val footerLabel = Label(footerText, LABEL_SIZE, WatchFaceColors.LabelBackgroundColorDark)
        // TODO: Properly handle if canvas is not there
        val footerLabelLeft = (canvas!!.width / 2) - (footerLabel.getWidth() / 2)
        // TODO: Use a global and/or useful gap value
        val footerLabelTop = slotBottom + context.resources.getDimension(R.dimen.gap) * 2
        canvas?.let {
            footerLabel.draw(
                it,
                footerLabelLeft,
                footerLabelTop
            )
        }
        return footerLabelTop + footerLabel.getHeight()
    }

    fun drawSlot(
        slotData: SlotMetadata,
        leftStart: Float,
        topStart: Float,
        backgroundColor: Int,
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
            backgroundColor
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
                slotMetadata.valueColor,
            )
        }
    }

    private fun createTextSlot(slotMetadata: TextSlotMetadata): List<DrawableItem> {
        return listOf(
            DrawableText(
                slotMetadata.text,
                getCharHeight(),
                getCharWidth(),
                slotMetadata.valueColor,
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