package de.niilz.wearos.watchface.bttf

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import kotlin.math.absoluteValue

class DrawableText(
    private val text: String,
    private val textHeight: Float,
    private val charwidth: Float,
    private val textColor: Int,
) : DrawableItem {

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = textColor
        textSize = textHeight
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        textPaint.textScaleX = calculateTextScaleX()
        canvas.drawText(text, x, y + getHeightWithoughtLowPart(), textPaint)
    }

    override fun getWidth(): Float {
        return textPaint.measureText(text)
    }

    override fun getHeight(): Float {
        val fontMetrics = textPaint.fontMetrics
        // Caclulates the maximum letter height
        // (ascent from base to above, negative value)
        // (descent from base to below, positive value)
        // subtracting ascent is like adding
        return fontMetrics.descent - fontMetrics.ascent
    }

    private fun getHeightWithoughtLowPart(): Float {
        val fontMetrics = textPaint.fontMetrics
        return fontMetrics.ascent.absoluteValue
    }

    private fun calculateTextScaleX(): Float {
        val targetWidth = charwidth * 3
        return targetWidth / getWidth()
    }
}