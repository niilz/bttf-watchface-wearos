package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat

class DrawableSlot(
    context: Context,
    private val items: List<DrawableItem>,
    private val label: DrawableItem,
    private val left: Float,
    private val top: Float,
) {
    private val background = ContextCompat.getColor(context, R.color.slot_bg_color)
    private val gap = context.resources.getDimension(R.dimen.gap)
    private val padding = 2 * gap


    fun draw(canvas: Canvas) {
        var cursor = left + padding
        var marginLabelToUpperRow = top + padding

        // Draw the Label
        label.draw(canvas, left + labelToItemOffset(), marginLabelToUpperRow)
        // Draw the Value Box
        val gapBetweenLabelAndValues = 3 * gap
        val valuesTop =
            marginLabelToUpperRow + label.getHeight() + gapBetweenLabelAndValues + padding


        val right = left + calcSlotWidth()
        val bottom = valuesTop + items[0].getHeight() + 2 * padding
        val bgRect = Rect(left.toInt(), valuesTop.toInt(), right.toInt(), bottom.toInt())
        val rectPaint = Paint().apply {
            color = background
            style = Paint.Style.FILL
        }
        canvas.drawRect(bgRect, rectPaint)

        for (item in items) {
            item.draw(canvas, cursor, valuesTop + padding)
            cursor += item.getWidth() + gap
        }
    }

    private fun labelToItemOffset(): Float {
        val slotCenter = calcSlotWidth() / 2f
        val labelCenter = label.getWidth() / 2f
        return slotCenter - labelCenter
    }

    fun getWidth(): Float {
        return calcSlotWidth()
    }

    private fun calcSlotWidth(): Float {
        return items.size * items[0].getWidth() + (items.size * gap) + 2 * padding
    }
}