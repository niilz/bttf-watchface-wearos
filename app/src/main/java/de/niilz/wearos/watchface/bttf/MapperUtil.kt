package de.niilz.wearos.watchface.bttf

import java.time.LocalDate


class MapperUtil {
    companion object DateMapper {
        fun mapLocalDate(date: LocalDate): List<Int> {
            return date.toString().replace("-", "").toCharArray().map { it.digitToInt() }.toList()
        }
    }
}