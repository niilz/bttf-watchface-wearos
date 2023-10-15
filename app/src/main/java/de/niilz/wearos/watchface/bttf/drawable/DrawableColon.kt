package de.niilz.wearos.watchface.bttf.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import de.niilz.wearos.watchface.bttf.util.DrawUtil
import java.time.ZonedDateTime

class DrawableColon(private val itemHeight: Float, private val now: ZonedDateTime) : DrawableItem {
  private val quadrants = 4
  private val radius = itemHeight / (2 * quadrants)
  override fun draw(canvas: Canvas, x: Float, y: Float) {
    val cx = x + radius
    val cy1 = itemHeight / quadrants
    val cy2 = 3 * cy1
    var amColor = Color.YELLOW
    var pmColor = Color.YELLOW
    if (now.hour > 12) {
      amColor = DrawUtil.darkenColor(amColor, 0.7f)
    } else {
      pmColor = DrawUtil.darkenColor(pmColor, 0.7f)
    }
    canvas.drawCircle(cx, y + cy1, radius, Paint().apply { color = amColor })
    canvas.drawCircle(cx, y + cy2, radius, Paint().apply { color = pmColor })
  }

  override fun getWidth(): Float {
    return radius * 2
  }

  override fun getHeight(): Float {
    return itemHeight
  }
}