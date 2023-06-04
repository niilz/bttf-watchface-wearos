package de.niilz.wearos.watchface.bttf

import android.graphics.Bitmap
import java.time.LocalDate
import kotlin.math.cos
import kotlin.math.sin


class MapperUtil {
    companion object Mappers {
        fun mapLocalDate(date: LocalDate): List<Int> {
            return date.toString().replace("-", "").toCharArray().map { it.digitToInt() }.toList()
        }

        fun mapTwoDigitNumToInts(number: Int): List<Int> {
            return mapToNumberList(number, 2)
        }

        fun mapYearToInts(number: Int): List<Int> {
            return mapToNumberList(number, 4)
        }

        private fun mapToNumberList(numbers: Int, width: Int): List<Int> {
            return numbers.toString()
                .padStart(width, '0')
                .toCharArray()
                .map { it.digitToInt() }
                .toList()
        }

        fun scaleBitmap(bitmap: Bitmap, widthScale: Float, heightScale: Float): Bitmap {
            return Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * widthScale).toInt(),
                (bitmap.height * heightScale).toInt(),
                true
            )
        }

        fun calcTopLeftCornerOnCirlce(canvasWidth: Float, canvasHeight: Float): Pair<Float, Float> {
            // imagine a circle around (0, 0) of the canvas square
            // (outside the watch circle) with a radius one:
            // x-----
            // |     |
            //  -----
            // imagine the target (top-left -45-deg  position to be the
            // target on our watch circle, but just here outside the view
            // on this mini circle
            val cornerCirlceX = -sin(3.14 / 4f).toFloat()
            val cornerCircleY = -cos(3.14 / 4f).toFloat()

            val canvasCenterX = canvasWidth / 2f
            val canvasCenterY = canvasHeight / 2f

            val topLeftX = canvasCenterX + cornerCirlceX * canvasCenterX
            val topLeftY = canvasCenterY + cornerCircleY * canvasCenterX
            return Pair(topLeftX, topLeftY)
        }

        fun percentOfInnerCanvasSkalar(
            canvasWidthOrHight: Float,
            itemSize: Float,
            targetPercentOfCanvas: Int
        ): Float {
            val targetSize = canvasWidthOrHight * (targetPercentOfCanvas / 100f)
            return (targetSize / itemSize)
        }

        fun numbersToDrawables(
            numbersRaw: List<Int>,
            numberBitmaps: List<Bitmap>,
            backgroundBitmap: Bitmap,
            color: Int,
            bgColor: Int,
        ): List<DrawableItem> {
            return numbersRaw
                .map { numberBitmaps[it] }
                .map { DrawableNumber(it, backgroundBitmap, color, bgColor) }
        }
    }

}