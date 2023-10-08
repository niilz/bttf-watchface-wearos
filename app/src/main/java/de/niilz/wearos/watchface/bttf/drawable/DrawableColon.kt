package de.niilz.wearos.watchface.bttf.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class DrawableColon(private val itemHeight: Float) : DrawableItem {
  private val quadrants = 4
  override fun draw(canvas: Canvas, x: Float, y: Float) {
    val radius = itemHeight / (2 * quadrants)
    val cx = x + radius
    val cy1 = itemHeight / quadrants
    val cy2 = 3 * cy1
    canvas.drawCircle(cx, y + cy1, radius, Paint().apply { color = Color.YELLOW })
    canvas.drawCircle(cx, y + cy2, radius, Paint().apply { color = Color.YELLOW })
  }

  override fun getWidth(): Float {
    return 5f
  }

  override fun getHeight(): Float {
    return 10f
  }
}