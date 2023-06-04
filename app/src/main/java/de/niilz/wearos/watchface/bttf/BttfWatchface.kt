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
        private var initialNumberHeight = 0.0f
        private val gap = resources.getDimension(R.dimen.gap)

        private lateinit var numberPaint: Paint

        private var topLeftX = 0f
        private var topLeftY = 0f
        private var radius = 0f
        private var canvasInnerWidthOrHeight = 0f
        private var topOffset = 0f
        private var leftOffet = 0f

        private lateinit var drawService: DrawService;

        override fun onCreate(holder: SurfaceHolder?) {
            super.onCreate(holder)
            initializeBackground()
            initializeNumbers()
            drawService = DrawService(context = applicationContext, numberBitmaps = numbers)
        }

        override fun onDraw(canvas: Canvas, bounds: Rect?) {
            drawBackground(canvas)
            drawService.canvas = drawService.canvas ?: canvas
            drawService.updateNumbers(canvasInnerWidthOrHeight, initialNumberHeight, 8)
            drawSlots()
        }

        private fun drawSlots() {
            val now = LocalDate.now()
            val top = topLeftY + topOffset

            // FIRST-ROW
            // Day Slot
            val dayNums = MapperUtil.mapDayToInts(now.dayOfMonth)
            val leftStart = topLeftX + leftOffet
            val daySlotData = SlotMetadata("DAY", dayNums)

            // Year Slot
            val yearNums = MapperUtil.mapYearToInts(now.year)
            val yearSlotData = SlotMetadata("YEAR", yearNums)

            drawService.drawNumberSlots(
                listOf(daySlotData, yearSlotData),
                leftStart,
                top,
                gap,
            )
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

            calcValues(width.toFloat(), height.toFloat())
            val backgroundScale = canvasInnerWidthOrHeight / backgroundBitmap.width.toFloat()
            backgroundBitmap = MapperUtil.scaleBitmap(backgroundBitmap, backgroundScale)

        }

        private fun initializeBackground() {
            backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
        }

        private fun calcValues(width: Float, height: Float) {
            radius = width / 2f;
            canvasInnerWidthOrHeight = sqrt(2f) * radius;
            val (x, y) = MapperUtil.calcTopLeftCornerOnCirlce(width, height)
            topLeftX = x
            topLeftY = y
            leftOffet = canvasInnerWidthOrHeight * 0.05f
            topOffset = canvasInnerWidthOrHeight * 0.05f
        }

        private fun initializeNumbers() {
            fun getDrawable(idx: Int): Drawable? {
                val id = resources.getIdentifier("number_$idx", "drawable", packageName)
                return ContextCompat.getDrawable(applicationContext, id)
            }
            numbers = (0..9).map { getDrawable(it)!!.toBitmap() }.toList()
            initialNumberHeight = numbers[0].height.toFloat()
        }
    }
}