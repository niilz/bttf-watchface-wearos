package de.niilz.wearos.watchface.bttf

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.wearable.watchface.CanvasWatchFaceService
import android.view.SurfaceHolder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.time.LocalDate
import kotlin.math.sqrt

class BttfWatchface : CanvasWatchFaceService() {

    override fun onCreateEngine(): CanvasWatchFaceService.Engine {
        return Engine()
    }

    inner class Engine : CanvasWatchFaceService.Engine() {
        private lateinit var backgroundBitmap: Bitmap
        private lateinit var numbers: List<Bitmap>

        private val shadowRadius = resources.getDimension(R.dimen.shadow_radius)
        private val numberWidth = resources.getDimension(R.dimen.number_width)
        private val numberHeight = resources.getDimension(R.dimen.number_height)
        private val gap = resources.getDimension(R.dimen.gap)

        private lateinit var numberPaint: Paint

        private lateinit var colors: NumberColors;
        private var topLeftX = 0f
        private var topLeftY = 0f
        private var radius = 0f
        private var canvasInnerWidthOrHeight = 0f

        override fun onCreate(holder: SurfaceHolder?) {
            super.onCreate(holder)
            initializeBackground()
            initializeNumbers()
            colors = NumberColors(applicationContext)
        }

        override fun onDraw(canvas: Canvas, bounds: Rect?) {
            drawBackground(canvas)
            drawNumbers(canvas)
        }

        private fun drawNumbers(canvas: Canvas) {
            val now = LocalDate.now()
            val dateNums = MapperUtil.mapLocalDate(now)
                .map { numbers[it] }
                .map { DrawableNumber(it, colors.numberColorRow1, 40f, 30f) }
            val dateSlot = Slot(
                applicationContext,
                dateNums,
                topLeftX.toInt(),
                topLeftY.toInt(),
                380,
                130,
                gap
            )
            dateSlot.draw(canvas)
        }

        private fun drawBackground(canvas: Canvas) {
            canvas.drawBitmap(backgroundBitmap, topLeftX, topLeftY, null)
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)

            calcInnerSquare(width.toFloat(), height.toFloat());
            val backgroundScale = canvasInnerWidthOrHeight / backgroundBitmap.width.toFloat()
            backgroundBitmap = MapperUtil.scaleBitmap(backgroundBitmap, backgroundScale)

            val numberScale = width.toFloat() / (150f * 14f)
            numbers = numbers.map { MapperUtil.scaleBitmap(it, numberScale) }
        }

        private fun initializeBackground() {
            backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
        }

        private fun calcInnerSquare(width: Float, height: Float) {
            radius = width / 2f;
            canvasInnerWidthOrHeight = sqrt(2f) * radius;
            val (x, y) = MapperUtil.calcTopLeftCornerOnCirlce(width, height)
            topLeftX = x
            topLeftY = y
        }

        private fun initializeNumbers() {
            fun getDrawable(idx: Int): Drawable? {
                val id = resources.getIdentifier("number_$idx", "drawable", packageName)
                return ContextCompat.getDrawable(applicationContext, id)
            }

            numbers = (0..9).map { getDrawable(it)!!.toBitmap() }.toList()
        }

    }
}