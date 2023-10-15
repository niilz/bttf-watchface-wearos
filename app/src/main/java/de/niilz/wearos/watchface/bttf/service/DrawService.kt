package de.niilz.wearos.watchface.bttf.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import de.niilz.wearos.watchface.bttf.R
import de.niilz.wearos.watchface.bttf.config.WatchFaceColors
import de.niilz.wearos.watchface.bttf.drawable.DrawableColon
import de.niilz.wearos.watchface.bttf.drawable.DrawableItem
import de.niilz.wearos.watchface.bttf.drawable.DrawableLabel
import de.niilz.wearos.watchface.bttf.drawable.DrawableSlot
import de.niilz.wearos.watchface.bttf.drawable.DrawableText
import de.niilz.wearos.watchface.bttf.util.MapperUtil

class DrawService(
  private val context: Context,
  private var numberBitmaps: List<Bitmap>,
  var canvas: Canvas?
) {
  // TODO: Do we really need both constructors?
  private val labelSize = context.resources.getInteger(R.integer.label_size).toFloat()

  constructor(context: Context, numberBitmaps: List<Bitmap>) : this(context, numberBitmaps, null)

  fun drawRow(
    leftStart: Float,
    top: Float,
    rowData: List<SlotMetadata>,
    footerText: String,
    backgroundColor: Int
  ): Float {
    var cursor = leftStart
    var slotBottom = 0f
    val drawableSlots = rowData.map { slotData ->
      createDrawableSlot(
        slotData,
        backgroundColor,
        slotData.marginRight
      )
    }
    for (slot in drawableSlots) {
      val (_slotRightEnd, slotBottomEnd) = drawSlot(slot, cursor, top)
      cursor += slot.calcSlotWidth() + slot.marginRight
      if (slotBottomEnd > slotBottom) {
        slotBottom = slotBottomEnd
      }
    }
    val footerLabel =
      DrawableLabel(
        footerText,
        labelSize,
        WatchFaceColors.LabelBackgroundColorDark
      )
    // TODO: Properly handle if canvas is not there
    val footerLabelLeft = (canvas!!.width / 2) - (footerLabel.getWidth() / 2)
    // TODO: Use a global and/or useful gap value
    val footerLabelTop = slotBottom + context.resources.getDimension(R.dimen.gap) * 2
    canvas?.let {
      footerLabel.draw(
        it,
        footerLabelLeft,
        footerLabelTop
      )
    }
    return footerLabelTop + footerLabel.getHeight()
  }

  private fun createDrawableSlot(
    slotMetadata: SlotMetadata,
    backgroundColor: Int,
    marginRight: Float
  ): DrawableSlot {
    val itemsToDraw = createDrawableItems(slotMetadata)
    val labelText = slotMetadata.labelText ?: ""
    val label = DrawableLabel(labelText, labelSize)

    return DrawableSlot(
      context,
      itemsToDraw,
      label,
      0f,//leftStart,
      0f, // topStart,
      backgroundColor,
      marginRight
    )
  }

  private fun drawSlot(
    drawableSlot: DrawableSlot,
    leftStart: Float,
    topStart: Float,
  ): Pair<Float, Float> {
    drawableSlot.left = leftStart
    drawableSlot.top = topStart
    canvas?.let {
      val (rightEnd, bottomEnd) = drawableSlot.draw(it)
      return Pair(rightEnd + 50, bottomEnd)
    }
    throw IllegalStateException("Could not draw slot to canvas")
  }

  fun updateNumbers(
    canvasInnerWidthOrHeight: Float,
    initialNumberWidth: Float,
    initialNumberHeight: Float,
    maxNumbersPerRow: Int,
    maxNumbersPerCol: Int
  ) {
    val targetNumberWidth = canvasInnerWidthOrHeight / maxNumbersPerRow
    val widthScalar = targetNumberWidth / initialNumberWidth

    val targetNumberHeight = canvasInnerWidthOrHeight / maxNumbersPerCol
    val heightScalar = targetNumberHeight / initialNumberHeight

    numberBitmaps =
      numberBitmaps.map {
        MapperUtil.scaleBitmap(
          it,
          widthScale = widthScalar,
          heightScale = heightScalar
        )
      }
  }

  private fun createDrawableItems(slotMetadata: SlotMetadata): List<DrawableItem> {
    val color = slotMetadata.valueColor
    return slotMetadata.slotValues.map { it ->
      when (it) {
        is NumVal ->
          MapperUtil.numberToDrawable(
            it.num,
            numberBitmaps,
            numberBitmaps[8],
            color
          )

        is TextVal ->
          DrawableText(
            it.text,
            getCharHeight(),
            getCharWidth(),
            slotMetadata.valueColor,
          )

        is ShapeVal -> slotMetadata.now?.let { now -> DrawableColon(getCharHeight(), now) }
          ?: throw IllegalStateException("Now must be set for colon")

        else -> throw IllegalStateException("Unsupported SlotValue-type: $it")
      }
    }
  }

  private fun getCharWidth(): Float {
    return numberBitmaps[0].width.toFloat()
  }

  private fun getCharHeight(): Float {
    return numberBitmaps[1].height.toFloat()
  }
}
