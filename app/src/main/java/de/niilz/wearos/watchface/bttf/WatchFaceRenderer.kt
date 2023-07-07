package de.niilz.wearos.watchface.bttf

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.SurfaceHolder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime
import kotlin.math.sqrt

private const val FRAME_PERIOD_MS_DEFAULT = 16L

class WatchFaceRenderer(
    private val context: Context,
    private val resources: Resources,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int
) : Renderer.CanvasRenderer2<WatchFaceRenderer.DigitalSharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    FRAME_PERIOD_MS_DEFAULT,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    class DigitalSharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

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
    private var topBottomMargin = 0f
    private var firstRowLeftMargin = 0f
    private var areValuesInit = false

    private var drawService: DrawService;

    init {
        println("*** init ***")
        initializeBackground()
        initializeNumbers()
        drawService = DrawService(context = context, numberBitmaps = numbers)
    }

    override suspend fun createSharedAssets(): DigitalSharedAssets {
        return DigitalSharedAssets()
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigitalSharedAssets
    ) {
        println("*** onDraw ***")
        if (drawService.canvas == null) {
            drawService.canvas = canvas
        }

        if (!areValuesInit) {
            initializeValues(canvas.width, canvas.height)
            areValuesInit = true
        }
        drawBackground(canvas)
        drawSlots()
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigitalSharedAssets
    ) {
        println("TODO: Can render hightlight layer here")
    }

    private fun drawSlots() {
        println("*** drawSlots ***")

        val topRow1 = topLeftY + topBottomMargin
        val bottomRow1 = drawRow1(WatchFaceColors.NumberColorRow1, topRow1) + 3 * topBottomMargin
        val bottomRow2 = drawRow2(WatchFaceColors.NumberColorRow2, bottomRow1) + 3 * topBottomMargin
        val bottomRow3 = drawRow3(WatchFaceColors.NumberColorRow3, bottomRow2);
    }

    private fun drawRow1(valueColor: Int, startTop: Float): Float {

        val now = LocalDateTime.now()

        // FIRST-ROW
        var leftStart = topLeftX + firstRowLeftMargin
        // TODO: Maybe globally define margin
        val margin = 2 * gap

        // Month-Name Slot
        val monthSlotData = TextSlotMetadata(
            "MONTH",
            now.month.toString().substring(0, 3),
            valueColor,
            margin
        )

        // Day Slot
        val dayNums = MapperUtil.mapTwoDigitNumToInts(now.dayOfMonth)
        val daySlotData =
            BitmapSlotMetadata("DAY", dayNums, valueColor, margin)

        // Year Slot
        val yearNums = MapperUtil.mapYearToInts(now.year)
        val yearSlotData =
            BitmapSlotMetadata("YEAR", yearNums, valueColor, margin)

        // Hour Slot
        val hourNums = MapperUtil.mapTwoDigitNumToInts(now.hour)
        val hourSlotData =
            BitmapSlotMetadata("HOUR", hourNums, valueColor, 2 * margin)

        // TODO: Draw am-pm dots (Dot-Slot / drwable Item)

        // Minute Slot
        val minuteNums = MapperUtil.mapTwoDigitNumToInts(now.minute)
        val minuteSlotData =
            BitmapSlotMetadata("MIN", minuteNums, valueColor, 0f)

        return drawService.drawRow(
            leftStart,
            startTop,
            listOf(monthSlotData, daySlotData, yearSlotData, hourSlotData, minuteSlotData),
            "DESTINATION TIME",
            DrawUtil.darkenColor(valueColor, 0.8f)
        )
    }

    fun drawRow2(valueColor: Int, bottomRow1: Float): Float {
        // TODO: actually develop logic to draw values for row 2
        return drawRow1(valueColor, bottomRow1)
    }

    fun drawRow3(valueColor: Int, bottomRow2: Float): Float {
        // TODO: actually develop logic to draw values for row 3
        return drawRow1(valueColor, bottomRow2)
    }

    private fun drawBackground(canvas: Canvas) {
        println("*** start drawBackground ***")
        canvas.drawBitmap(backgroundBitmap, topLeftX, topLeftY, null)
        println("*** end drawBackground ***")
    }

    fun initializeValues(
        width: Int,
        height: Int
    ) {
        println("*** initializeValues ***")

        calcValues(width.toFloat(), height.toFloat())
        val backgroundScale = canvasInnerWidthOrHeight / backgroundBitmap.width.toFloat()
        backgroundBitmap =
            MapperUtil.scaleBitmap(backgroundBitmap, backgroundScale, backgroundScale)

        drawService.updateNumbers(
            canvasInnerWidthOrHeight,
            initialNumberWidth,
            initialNumberHeight,
            19,
            10
        )

    }

    private fun initializeBackground() {
        println("*** initializeBackground ***")
        backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
    }

    private fun calcValues(width: Float, height: Float) {
        println("*** calcValues ***")
        radius = width / 2f
        canvasInnerWidthOrHeight = sqrt(2f) * radius;
        val (x, y) = MapperUtil.calcTopLeftCornerOnCirlce(width, height)
        topLeftX = x
        topLeftY = y
        topBottomMargin = canvasInnerWidthOrHeight * topMarginScalar
        firstRowLeftMargin = canvasInnerWidthOrHeight * leftMarginScalar
    }

    private fun initializeNumbers() {
        println("*** initializeNumbers ***")
        fun getDrawable(idx: Int): Drawable? {
            val id = resources.getIdentifier("number_$idx", "drawable", context.packageName)
            return ContextCompat.getDrawable(context, id)
        }
        numbers = (0..9).map { getDrawable(it)!!.toBitmap() }.toList()
        val firstNumber = numbers[0]
        initialNumberWidth = firstNumber.width.toFloat()
        initialNumberHeight = firstNumber.height.toFloat()
    }

}