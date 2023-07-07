package de.niilz.wearos.watchface.bttf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.graphics.ColorUtils

class DrawableNumber(
    private val numberBitmap: Bitmap,
    private val backgroundBitmap: Bitmap,
    private val numColor: Int,
) : DrawableItem {

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        val numberPaint = Paint().apply {
            colorFilter = PorterDuffColorFilter(numColor, PorterDuff.Mode.SRC_IN)
            isAntiAlias = true
        }
        val backgroundColor = ColorUtils.blendARGB(numColor, Color.BLACK, 0.6f)
        val backgroundPaint = Paint().apply {
            colorFilter = PorterDuffColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN)
            isAntiAlias = true
        }
        canvas.drawBitmap(backgroundBitmap, x, y, backgroundPaint)
        canvas.drawBitmap(numberBitmap, x, y, numberPaint)
    }

    override fun getWidth(): Float {
        return numberBitmap.width.toFloat()
    }

    override fun getHeight(): Float {
        return numberBitmap.height.toFloat()
    }
}