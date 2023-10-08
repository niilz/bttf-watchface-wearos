package de.niilz.wearos.watchface.bttf.drawable

import android.graphics.Canvas
import android.graphics.Paint
import de.niilz.wearos.watchface.bttf.config.WatchFaceColors

class DrawableLabel(
  val text: String,
  private val size: Float,
  bgColor: Int,
) :
  DrawableItem {

  private var textPaint: Paint = Paint().apply {
    color = WatchFaceColors.LabelTextColor
    textSize = size
  };

  private val bgPaint = Paint().apply {
    color = bgColor
  }

  constructor(text: String, size: Float) : this(
    text,
    size,
    WatchFaceColors.LabelDefaultBackgroundColor
  )


  private val padding = textPaint.textSize * 0.2f

  override fun draw(canvas: Canvas, x: Float, y: Float) {
    canvas.drawRect(x, y, x + getWidth(), y + getHeight(), bgPaint)
    val textTop = y + textPaint.textSize
    val textLeft = x + padding
    canvas.drawText(text, textLeft, textTop, textPaint)
  }

  override fun getWidth(): Float {
    return textPaint.measureText(text) + (2f * padding)
  }

  override fun getHeight(): Float {
    return textPaint.textSize + (2f * padding)
  }
}