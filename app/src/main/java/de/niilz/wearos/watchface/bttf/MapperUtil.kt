package de.niilz.wearos.watchface.bttf

import android.graphics.Bitmap
import java.time.LocalDate


class MapperUtil {
    companion object Mappers {
        fun mapLocalDate(date: LocalDate): List<Int> {
            return date.toString().replace("-", "").toCharArray().map { it.digitToInt() }.toList()
        }

        fun scaleBitmap(bitmap: Bitmap, scale: Float): Bitmap {
            return Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * scale).toInt(),
                (bitmap.height * scale).toInt(),
                true
            )
        }
    }

}