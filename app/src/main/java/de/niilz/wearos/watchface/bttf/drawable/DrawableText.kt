package de.niilz.wearos.watchface.bttf.drawable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface

private const val TEXT_BOUND_BOTTOM = 1f

class DrawableText(
    private val text: String,
    private val charHeight: Float,
    private val charWidth: Float,
    private val textColor: Int,
) : DrawableItem {

    private var height = 0f

    private fun textPaint(): Paint {
        return Paint().apply {
            isAntiAlias = true
            color = textColor
            textSize = charHeight * calcTextSizeScalar(charHeight)
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        val textPaint = textPaint()
        textPaint.textScaleX = calcTextScaleX()
        canvas.drawText(text, x, y + getHeight(), textPaint)
    }

    override fun getWidth(): Float {
        return calcTextWidth() * calcTextScaleX()
    }

    override fun getHeight(): Float {
        if (height == 0f) {
            height = getRawCharHeight()
        }
        return height
    }

    fun getRawCharHeight(): Float {
        val bounds = Rect()
        textPaint().getTextBounds(text, 0, text.length, bounds)
        // We don't use get bounds.height() (which is: Abs(bottom - top)).
        // Instead we hardcode the bottom and only look at the top value.
        // So that offset-characters like the degree-symbol (°) do not
        // stick out on top.
        return TEXT_BOUND_BOTTOM + Math.abs(bounds.top)
    }

    private fun calcTextWidth(): Float {
        return textPaint().measureText(text)
    }

    private fun calcTextScaleX(): Float {
        val targetWidth = charWidth * text.chars().count()
        return targetWidth / calcTextWidth()
    }

    private fun calcTextSizeScalar(targetTextSize: Float): Float {
        val tempTextPaintWithPadding = Paint().apply {
            textSize = targetTextSize
        }
        val bounds = Rect()
        tempTextPaintWithPadding.getTextBounds(text, 0, text.length, bounds)
        val rawCharHeight = bounds.height().toFloat()
        val padding = targetTextSize - rawCharHeight
        val paddingPercentOfTextSize = padding / targetTextSize
        return 1 + paddingPercentOfTextSize
    }
}