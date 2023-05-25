package de.niilz.wearos.watchface.bttf

import android.graphics.*

class DrawableNumber(
    private val numberBitmap: Bitmap,
    private val numColor: Int,
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
        return numberBitmap.width.toFloat()
    }

    override fun getHeight(): Float {
        return numberBitmap.height.toFloat()
    }
}