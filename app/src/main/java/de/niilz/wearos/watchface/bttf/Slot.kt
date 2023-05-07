package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat

class Slot(
    context: Context,
    private val items: List<DrawableItem>,
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) {
    private val background = ContextCompat.getColor(context, R.color.slot_bg_color)

    fun draw(canvas: Canvas) {
        var cursor = left.toFloat()
        val bgRect = Rect(left, top, right, bottom)
        val rectPaint = Paint().apply {
            color = background
            style = Paint.Style.FILL
        }
        canvas.drawRect(bgRect, rectPaint)

        for (item in items) {
            item.draw(canvas, cursor, top.toFloat())
            cursor += item.getWidth()
        }
    }
}