package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat

class Slot(
    context: Context,
    private val items: List<DrawableItem>,
    private val left: Float,
    private val top: Float,
    private val gap: Float,
) {
    private val background = ContextCompat.getColor(context, R.color.slot_bg_color)

    private var width = 0f

    fun draw(canvas: Canvas) {
        var cursor = left + gap
        val marginTop = top + gap
        width = items.size * items[0].getWidth() + items.size * gap
        val right = left + width + (2 * gap)
        val bottom = top + items[0].getHeight() + (2 * gap)
        val bgRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        val rectPaint = Paint().apply {
            color = background
            style = Paint.Style.FILL
        }
        canvas.drawRect(bgRect, rectPaint)

        for (item in items) {
            item.draw(canvas, cursor, marginTop)
            cursor += item.getWidth() + gap
        }
    }
}