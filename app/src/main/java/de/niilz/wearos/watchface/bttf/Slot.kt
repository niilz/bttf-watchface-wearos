package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat

class Slot(
    context: Context,
    private val items: List<DrawableItem>,
    private val label: DrawableItem,
    private val left: Float,
    private val top: Float,
    private val gap: Float,
) {
    private val background = ContextCompat.getColor(context, R.color.slot_bg_color)

    private var slotWidth = items.size * items[0].getWidth() + items.size * gap

    fun draw(canvas: Canvas) {
        var cursor = left + gap
        var marginTop = top + gap

        // TODO: add XXX to 'cursor' so that label is center
        label.draw(canvas, cursor, marginTop)
        val valuesTop = marginTop + label.getHeight() + gap


        val right = left + slotWidth + (2 * gap)
        val bottom = valuesTop + items[0].getHeight() + (2 * gap)
        val bgRect = Rect(left.toInt(), valuesTop.toInt(), right.toInt(), bottom.toInt())
        val rectPaint = Paint().apply {
            color = background
            style = Paint.Style.FILL
        }
        canvas.drawRect(bgRect, rectPaint)

        for (item in items) {
            item.draw(canvas, cursor, valuesTop + gap)
            cursor += item.getWidth() + gap
        }
    }

    fun getWidth(): Float {
        return slotWidth
    }
}