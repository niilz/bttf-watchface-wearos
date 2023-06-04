package de.niilz.wearos.watchface.bttf

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface

class DrawableText(
    private val text: String,
    private val textHeight: Float,
    private val textColor: Int,
) : DrawableItem {

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = textColor
        textSize = textHeight
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        canvas.drawText(text, x, y + getHeight(), textPaint)
    }

    override fun getWidth(): Float {
        return textPaint.measureText(text)
    }

    override fun getHeight(): Float {
        return textPaint.textSize
    }
}