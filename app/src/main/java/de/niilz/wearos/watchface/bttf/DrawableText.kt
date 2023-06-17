package de.niilz.wearos.watchface.bttf

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface

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
        textPaint.textScaleX = calcTextScaleX()
        canvas.drawText(text, x, y + getHeight(), textPaint)
    }

    override fun getWidth(): Float {
        return calcTextWidth() * calcTextScaleX()
    }

    override fun getHeight(): Float {
        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height().toFloat()
    }

    private fun calcTextWidth(): Float {
        return textPaint.measureText(text)
    }

    private fun calcTextScaleX(): Float {
        val targetWidth = charwidth * text.chars().count()
        return targetWidth / calcTextWidth()
    }
}