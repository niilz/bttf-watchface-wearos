package de.niilz.wearos.watchface.bttf

import android.graphics.*
import android.support.wearable.watchface.CanvasWatchFaceService
import android.view.SurfaceHolder
import androidx.core.content.ContextCompat

class BttfWatchface : CanvasWatchFaceService() {

    override fun onCreateEngine(): CanvasWatchFaceService.Engine {
        return Engine()
    }

    inner class Engine : CanvasWatchFaceService.Engine() {
        private lateinit var backgroundBitmap: Bitmap

        private val watchNumberColor =
            ContextCompat.getColor(applicationContext, R.color.watch_number_color)
        private val watchNumberShadow =
            ContextCompat.getColor(applicationContext, R.color.watch_shadow_color)

        private val shadowRadius = resources.getDimension(R.dimen.shadow_radius)
        private val numberWidth = resources.getDimension(R.dimen.number_width)
        private val numberHeight = resources.getDimension(R.dimen.number_height)

        private lateinit var numberPaint: Paint

        override fun onCreate(holder: SurfaceHolder?) {
            super.onCreate(holder)
            initializeBackground()
            initializeNumbers()
        }

        private fun initializeNumbers() {
            numberPaint = Paint().apply {
                color = watchNumberColor
                strokeWidth = numberWidth
                isAntiAlias = true
                style = Paint.Style.STROKE
                setShadowLayer(
                    shadowRadius, 0f, 0f, watchNumberShadow
                )
            }
        }

        override fun onDraw(canvas: Canvas, bounds: Rect?) {
            drawBackground(canvas)
            drawTicks()
        }

        private fun drawTicks() {

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