package de.niilz.wearos.watchface.bttf

import android.graphics.Canvas
import android.graphics.Color.rgb
import android.graphics.Paint

class Label(
    private val text: String,
    private val size: Float,
) :
    DrawableItem {

    private val textPaint = Paint().apply {
        // TODO: Use actual colors.xml value for label-text (white)
        color = rgb(255, 255, 255)
        textSize = size
    }

    private val bgPaint = Paint().apply {
        // TODO: Use actual colors.xml value for label-bg (dark-red)
        color = rgb(80, 0, 0)
    }

    private val padding = textPaint.textSize * 0.2f

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), bgPaint)
        val textTop = y + textPaint.textSize
        val textLeft = x + padding
        canvas.drawText(text, textLeft, textTop, textPaint)
    }

    override fun getWidth(): Float {
        return textPaint.measureText(text) + (2f * padding)
    }

    override fun getHeight(): Float {
        return textPaint.textSize + (2f * padding)
    }
}