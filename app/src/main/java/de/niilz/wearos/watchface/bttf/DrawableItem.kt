package de.niilz.wearos.watchface.bttf

import android.graphics.Canvas

interface DrawableItem {

    fun draw(canvas: Canvas, x: Float, y: Float)

    fun getWidth(): Float
}