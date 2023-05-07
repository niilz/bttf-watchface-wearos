package de.niilz.wearos.watchface.bttf

import android.graphics.*

class DrawableNumber(
    private val numberBitmap: Bitmap,
    private val numColor: Int,
    private val width: Float,
    private val height: Float,
) : DrawableItem {

    override fun draw(canvas: Canvas, x: Float, y: Float) {
        val numberPaint = Paint().apply {
            colorFilter = PorterDuffColorFilter(numColor, PorterDuff.Mode.SRC_IN)
            isAntiAlias = true
            //color = watchNumberColor
            //strokeWidth = numberWidth
            //style = Paint.Style.FILL
        }
        canvas.drawBitmap(numberBitmap, x, y, numberPaint)
    }

    override fun getWidth(): Float {
        return width
    }
}