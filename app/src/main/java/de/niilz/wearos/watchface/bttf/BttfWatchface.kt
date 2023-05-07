package de.niilz.wearos.watchface.bttf

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.wearable.watchface.CanvasWatchFaceService
import android.view.SurfaceHolder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.time.LocalDate

class BttfWatchface : CanvasWatchFaceService() {

    override fun onCreateEngine(): CanvasWatchFaceService.Engine {
        return Engine()
    }

    inner class Engine : CanvasWatchFaceService.Engine() {
        private lateinit var backgroundBitmap: Bitmap
        private lateinit var numbers: List<Bitmap?>

        private val shadowRadius = resources.getDimension(R.dimen.shadow_radius)
        private val numberWidth = resources.getDimension(R.dimen.number_width)
        private val numberHeight = resources.getDimension(R.dimen.number_height)

        private lateinit var numberPaint: Paint

        private lateinit var colors: NumberColors;

        override fun onCreate(holder: SurfaceHolder?) {
            super.onCreate(holder)
            initializeBackground()
            initializeNumbers()
            colors = NumberColors(applicationContext)
        }

        private fun initializeNumbers() {
            fun getDrawable(idx: Int): Drawable? {
                val id = resources.getIdentifier("number_$idx", "drawable", packageName)
                return ContextCompat.getDrawable(applicationContext, id)
            }

            numbers = (0..9).map { getDrawable(it)?.toBitmap() }.toList()
        }

        override fun onDraw(canvas: Canvas, bounds: Rect?) {
            drawBackground(canvas)
            drawNumbers(canvas)
        }

        private fun drawNumbers(canvas: Canvas) {
            val now = LocalDate.now()
            val dateNums = MapperUtil.mapLocalDate(now)
            dateNums.map { numbers?.get(it) }
                .map { DrawableNumber(it!!, colors.numberColorRow1, 40f, 30f) }
                .forEach { it.draw(canvas) }
        }

        private fun drawBackground(canvas: Canvas) {
            canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)

            val scale = width.toFloat() / backgroundBitmap.width.toFloat()
            backgroundBitmap = Bitmap.createScaledBitmap(
                backgroundBitmap,
                (backgroundBitmap.width * scale).toInt(),
                (backgroundBitmap.height * scale).toInt(),
                true
            )
        }

        private fun initializeBackground() {
            backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
        }
    }
}