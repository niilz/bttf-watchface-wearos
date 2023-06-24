package de.niilz.wearos.watchface.bttf

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.wearable.watchface.CanvasWatchFaceService
import android.view.SurfaceHolder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.time.LocalDateTime
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
        private val leftMarginScalar = resources.getDimension(R.dimen.top_margin_scalar)
        private val topMarginScalar = resources.getDimension(R.dimen.left_margin_scalar)
        private var initialNumberWidth = 0.0f
        private var initialNumberHeight = 0.0f
        private val gap = resources.getDimension(R.dimen.gap)

        private lateinit var numberPaint: Paint

        private var topLeftX = 0f
        private var topLeftY = 0f
        private var radius = 0f
        private var canvasInnerWidthOrHeight = 0f
        private var firstRowTopMargin = 0f
        private var firstRowLeftMargin = 0f

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
            drawService.updateNumbers(
                canvasInnerWidthOrHeight,
                initialNumberWidth,
                initialNumberHeight,
                18,
                10
            )
            drawSlots()
        }

        private fun drawSlots() {
            val now = LocalDateTime.now()
            val top = topLeftY + firstRowTopMargin

            // FIRST-ROW
            var leftStart = topLeftX + firstRowLeftMargin

            // Month-Name Slot
            val monthSlotData = TextSlotMetadata(
                "MONTH",
                now.month.toString().substring(0, 3),
            )

            // Day Slot
            val dayNums = MapperUtil.mapTwoDigitNumToInts(now.dayOfMonth)
            val daySlotData = BitmapSlotMetadata("DAY", dayNums)

            // Year Slot
            val yearNums = MapperUtil.mapYearToInts(now.year)
            val yearSlotData = BitmapSlotMetadata("YEAR", yearNums)

            // Hour Slot
            val hourNums = MapperUtil.mapTwoDigitNumToInts(now.hour)
            val hourSlotData = BitmapSlotMetadata("HOUR", hourNums)

            // Minute Slot
            val minuteNums = MapperUtil.mapTwoDigitNumToInts(now.minute)
            val minuteSlotData = BitmapSlotMetadata("MIN", minuteNums)

            // TODO: Maybe globally define margin
            val margin = 2 * gap

            leftStart += drawService.drawSlot(
                monthSlotData,
                leftStart,
                top,
            ) + margin

            leftStart += drawService.drawSlot(
                daySlotData,
                leftStart,
                top,
            ) + margin

            leftStart += drawService.drawSlot(
                yearSlotData,
                leftStart,
                top,
            ) + margin

            leftStart += drawService.drawSlot(
                hourSlotData,
                leftStart,
                top,
            ) + 2 * margin

            // TODO: Draw am-pm dots

            leftStart += drawService.drawSlot(
                minuteSlotData,
                leftStart,
                top,
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
            backgroundBitmap =
                MapperUtil.scaleBitmap(backgroundBitmap, backgroundScale, backgroundScale)

        }

        private fun initializeBackground() {
            backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
        }

        private fun calcValues(width: Float, height: Float) {
            radius = width / 2f
            canvasInnerWidthOrHeight = sqrt(2f) * radius;
            val (x, y) = MapperUtil.calcTopLeftCornerOnCirlce(width, height)
            topLeftX = x
            topLeftY = y
            firstRowTopMargin = canvasInnerWidthOrHeight * topMarginScalar
            firstRowLeftMargin = canvasInnerWidthOrHeight * leftMarginScalar
        }

        private fun initializeNumbers() {
            fun getDrawable(idx: Int): Drawable? {
                val id = resources.getIdentifier("number_$idx", "drawable", packageName)
                return ContextCompat.getDrawable(applicationContext, id)
            }
            numbers = (0..9).map { getDrawable(it)!!.toBitmap() }.toList()
            val firstNumber = numbers[0]
            initialNumberWidth = firstNumber.width.toFloat()
            initialNumberHeight = firstNumber.height.toFloat()
        }
    }
}