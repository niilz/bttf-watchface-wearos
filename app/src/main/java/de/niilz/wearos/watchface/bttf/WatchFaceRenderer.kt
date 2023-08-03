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
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import de.niilz.wearos.watchface.bttf.config.WatchFaceColors
import de.niilz.wearos.watchface.bttf.service.DrawService
import de.niilz.wearos.watchface.bttf.service.NumVal
import de.niilz.wearos.watchface.bttf.service.SlotMetadata
import de.niilz.wearos.watchface.bttf.service.SlotValue
import de.niilz.wearos.watchface.bttf.service.TextVal
import de.niilz.wearos.watchface.bttf.util.DrawUtil
import de.niilz.wearos.watchface.bttf.util.MapperUtil
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
        //println("*** init ***")
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
        //println("*** onDraw ***")
        if (drawService.canvas == null) {
            drawService.canvas = canvas
        }

        if (!areValuesInit) {
            initializeValues(canvas.width, canvas.height)
            areValuesInit = true
        }

        drawBackground(canvas)

        val complications =
            complicationSlotsManager.complicationSlots.map { (_, complication) -> complication }
                .filter { it.enabled }

        // NOTE: when we wait for the complicationdata to be available we have no
        //  memory leaks (segfaults in libc) on the bitmap number data?
        val complicationSlotsRow2 =
            mapComplicationsToSlotMetadata(complications, WatchFaceColors.NumberColorRow2, 2 * gap)
        if (complicationSlotsRow2.isNotEmpty()) {
            drawSlots(zonedDateTime, complicationSlotsRow2)
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigitalSharedAssets
    ) {
        println("TODO: Can render hightlight layer here")
    }

    private fun drawSlots(dateTime: ZonedDateTime, complications: List<SlotMetadata>) {
        //println("*** drawSlots ***")

        val topRow1 = topLeftY + topBottomMargin
        val bottomRow1 =
            drawRow1(dateTime, WatchFaceColors.NumberColorRow1, topRow1) + 3 * topBottomMargin
        val bottomRow2 = drawRow2(
            WatchFaceColors.NumberColorRow2,
            bottomRow1,
            complications
        ) + 3 * topBottomMargin
        val bottomRow3 = drawRow3(dateTime, WatchFaceColors.NumberColorRow3, bottomRow2);
    }

    private fun drawRow1(now: ZonedDateTime, valueColor: Int, startTop: Float): Float {
        // FIRST-ROW
        var leftStart = topLeftX + firstRowLeftMargin
        // TODO: Maybe globally define margin
        val margin = 2 * gap

        // Month-Name Slot
        val monthSlotData = SlotMetadata(
            "MONTH",
            valueColor,
            margin,
            listOf(
                TextVal(now.month.toString().substring(0, 3))
            )
        )

        // Day Slot
        val dayNums = MapperUtil.mapTwoDigitNumToInts(now.dayOfMonth).map { NumVal(it) }
        val daySlotData =
            SlotMetadata("DAY", valueColor, margin, dayNums)

        // Year Slot
        val yearNums = MapperUtil.mapYearToInts(now.year).map { NumVal(it) }
        val yearSlotData =
            SlotMetadata("YEAR", valueColor, margin, yearNums)

        // Hour Slot
        val hourNums = MapperUtil.mapTwoDigitNumToInts(now.hour).map { NumVal(it) }
        val hourSlotData =
            SlotMetadata("HOUR", valueColor, 2 * margin, hourNums)

        // TODO: Draw am-pm dots (Dot-Slot / drwable Item)

        // Minute Slot
        val minuteNums =
            MapperUtil.mapTwoDigitNumToInts(now.minute).map { NumVal(it) }
        val minuteSlotData =
            SlotMetadata("MIN", valueColor, 0f, minuteNums)

        return drawService.drawRow(
            leftStart,
            startTop,
            listOf(monthSlotData, daySlotData, yearSlotData, hourSlotData, minuteSlotData),
            "DESTINATION TIME",
            DrawUtil.darkenColor(valueColor, 0.8f)
        )
    }

    fun drawRow2(
        valueColor: Int,
        startTop: Float,
        complicationSlotDataList: List<SlotMetadata>
    ): Float {
        val leftStart = topLeftX + firstRowLeftMargin

        return drawService.drawRow(
            leftStart,
            startTop,
            complicationSlotDataList,
            "DESTINATION TIME",
            DrawUtil.darkenColor(valueColor, 0.8f)
        )
    }

    private fun mapComplicationsToSlotMetadata(
        complications: List<ComplicationSlot>,
        valueColor: Int,
        margin: Float
    ): List<SlotMetadata> {
        return complications.asSequence()
            .map { it.complicationData.value }
            .filter { it.dataSource != null }
            .map {
                Pair(
                    it.dataSource!!.shortClassName,
                    MapperUtil.complicationDataValueToString(it, resources)
                )
            }.map {
                val label = MapperUtil.classNameToCamelCaseParts(it.first).first().uppercase()
                val slotValues = parseNumbersAndTexts(it.second).toMutableList()
                if (label == "BATTERY" && (slotValues.last() as? TextVal)?.text?.endsWith("%") == true) {
                    slotValues.add(TextVal("%"))
                }
                SlotMetadata(label, valueColor, margin, slotValues)
            }.toList()
    }

    fun drawRow3(now: ZonedDateTime, valueColor: Int, bottomRow2: Float): Float {
        // TODO: actually develop logic to draw values for row 3
        return drawRow1(now, valueColor, bottomRow2)
    }

    private fun drawBackground(canvas: Canvas) {
        //println("*** start drawBackground ***")
        canvas.drawBitmap(backgroundBitmap, topLeftX, topLeftY, null)
        //println("*** end drawBackground ***")
    }

    private fun parseNumbersAndTexts(slotValue: String): List<SlotValue> {
        val slotValues = mutableListOf<SlotValue>()
        var tempNumbers = mutableListOf<Int>()
        var tempTexts = mutableListOf<Char>()
        var parsingNums = false
        for (char in slotValue.trim()) {
            if (char.isDigit()) {
                if (!parsingNums && tempTexts.isNotEmpty()) {
                    slotValues.add(TextVal(tempTexts.joinToString("")))
                    tempTexts = mutableListOf()
                }
                parsingNums = true
                tempNumbers.add(char.digitToInt())
            } else {
                if (parsingNums && tempNumbers.isNotEmpty()) {
                    slotValues.addAll(tempNumbers.map { num -> NumVal(num) })
                    tempNumbers = mutableListOf()
                }
                parsingNums = false
                tempTexts.add(char)
            }
        }
        if (tempNumbers.isNotEmpty()) {
            assert(parsingNums)
            slotValues.addAll(tempNumbers.map { num -> NumVal(num) })
        }
        if (tempTexts.isNotEmpty()) {
            assert(!parsingNums)
            slotValues.add(TextVal(tempTexts.joinToString("")))
        }
        return slotValues
    }


    fun initializeValues(
        width: Int,
        height: Int
    ) {
        //println("*** initializeValues ***")

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
        //println("*** initializeBackground ***")
        backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
    }

    private fun calcValues(width: Float, height: Float) {
        //println("*** calcValues ***")
        radius = width / 2f
        canvasInnerWidthOrHeight = sqrt(2f) * radius
        val (x, y) = MapperUtil.calcTopLeftCornerOnCirlce(width, height)
        topLeftX = x
        topLeftY = y
        topBottomMargin = canvasInnerWidthOrHeight * topMarginScalar
        firstRowLeftMargin = canvasInnerWidthOrHeight * leftMarginScalar
    }

    private fun initializeNumbers() {
        //println("*** initializeNumbers ***")
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
