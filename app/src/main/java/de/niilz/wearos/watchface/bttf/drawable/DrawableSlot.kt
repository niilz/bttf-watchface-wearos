package de.niilz.wearos.watchface.bttf.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import de.niilz.wearos.watchface.bttf.R

class DrawableSlot(
    context: Context,
    private val items: List<DrawableItem>,
    private val label: DrawableItem,
    private val left: Float,
    private val top: Float,
    private val backgroundColor: Int,
) {
    private val frameColor = ContextCompat.getColor(context, R.color.frame_color)
    private val gap = context.resources.getDimension(R.dimen.gap)
    private val padding = 2 * gap


    fun draw(canvas: Canvas): Pair<Float, Float> {
        var cursor = left + padding
        val marginTopToLabels = top + padding

        // Draw the Label
        label.draw(canvas, left + labelToItemOffset(), marginTopToLabels)
        // Draw the Value Box
        val gapBetweenLabelAndValues = 2 * gap
        val valuesTop =
            marginTopToLabels + label.getHeight() + gapBetweenLabelAndValues

        val right = left + calcSlotWidth()
        val bottom = valuesTop + items[0].getHeight() + 2 * padding
        // TODO: Make frame slightly larger than colored bg or colored bg slightly smaller than frame
        val frameRect = Rect(left.toInt(), valuesTop.toInt(), right.toInt(), bottom.toInt())
        val frameRectPaint = Paint().apply {
            color = frameColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(frameRect, frameRectPaint)

        val coloredBackgroundRect =
            Rect(left.toInt(), valuesTop.toInt(), right.toInt(), bottom.toInt())
        val coloredBackgroundRectPaint = Paint().apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(coloredBackgroundRect, coloredBackgroundRectPaint)

        for (item in items) {
            item.draw(canvas, cursor, valuesTop + padding)
            cursor += item.getWidth() + gap
        }
        // Return right-end and bottom-end of Slot
        return Pair(right - left, bottom)
    }

    private fun labelToItemOffset(): Float {
        val slotCenter = calcSlotWidth() / 2f
        val labelCenter = label.getWidth() / 2f
        return slotCenter - labelCenter
    }

    private fun calcSlotWidth(): Float {
        val totalWidthOfItems = items.map { it.getWidth() }.sum()
        val totalGapBetweenItems = ((items.size - 1) * gap)
        val paddingLeftAndRight = 2 * padding
        return totalWidthOfItems + totalGapBetweenItems + paddingLeftAndRight
    }
}