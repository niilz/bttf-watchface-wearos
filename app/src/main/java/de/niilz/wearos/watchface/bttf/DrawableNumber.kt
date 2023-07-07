package de.niilz.wearos.watchface.bttf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter

class DrawableNumber(
    private val numberBitmap: Bitmap,
    private val backgroundBitmap: Bitmap,
    private val numColor: Int,
    private val backgroundColor: Int,
) : DrawableItem {

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        val numberPaint = Paint().apply {
            colorFilter = PorterDuffColorFilter(numColor, PorterDuff.Mode.SRC_IN)
            isAntiAlias = true
        }
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