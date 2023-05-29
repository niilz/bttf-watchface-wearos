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

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        canvas.drawRect(x, y, x + 50, y + 20, bgPaint)
        canvas.drawText(text, x, y, textPaint)
    }

    override fun getWidth(): Float {
        // TODO: Give actual text width (mayby with getTextHeight(labelText, char-count)
        // TODO: Calculate BG-Width based on label text
        // TODO: Return BG-Rect-Width
        return 50f
    }

    override fun getHeight(): Float {
        // TODO: Give actual text hight (mayby with getTextHeight(labelText, )
        // TODO: Calculate BG-Hight based on label text
        // TODO: Return BG-Rect-Hight
        return bgPaint.textSize
    }
}