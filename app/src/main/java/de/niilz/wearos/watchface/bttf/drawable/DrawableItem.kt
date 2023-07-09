package de.niilz.wearos.watchface.bttf.drawable

import android.graphics.Canvas

interface DrawableItem {

    fun draw(canvas: Canvas, x: Float, y: Float)

    fun getWidth(): Float
    fun getHeight(): Float
}