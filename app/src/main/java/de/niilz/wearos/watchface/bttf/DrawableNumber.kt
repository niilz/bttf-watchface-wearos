package de.niilz.wearos.watchface.bttf

import android.graphics.*

class DrawableNumber(
    private val numberBitmap: Bitmap,
    private val numColor: Int,
    private val x: Float,
    private val y: Float
) {

    fun draw(canvas: Canvas) {
        val numberPaint = Paint().apply {
            colorFilter = PorterDuffColorFilter(numColor, PorterDuff.Mode.SRC_IN)
            isAntiAlias = true
            //color = watchNumberColor
            //strokeWidth = numberWidth
            //style = Paint.Style.FILL
        }
        canvas.drawBitmap(numberBitmap, x, y, numberPaint)
    }
}