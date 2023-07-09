package de.niilz.wearos.watchface.bttf.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class WatchFaceColors() {
    companion object {
        val NumberColorRow1 = Color(0xFFFF0000).toArgb()
        val NumberColorRow2 = Color(0xFF00FF00).toArgb()
        val NumberColorRow3 = Color(0xFFFFFF00).toArgb()
        val NumberBackgroundColor = Color(0xFF333333).toArgb()
        val SlotBackgroundColor = Color(0xFF444444).toArgb()
        val WatchNumberShadow = Color(0xFF2B3948).toArgb()
        val LabelDefaultBackgroundColor = Color(0xFF660000).toArgb()
        val LabelBackgroundColorDark = Color(0xFF222222).toArgb()
        val LabelTextColor = Color(0xffffffff).toArgb()

    }

}