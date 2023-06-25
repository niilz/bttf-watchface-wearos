package de.niilz.wearos.watchface.bttf

open class SlotMetadata(
    val labelText: String,
    val marginRight: Float,
) {}

class BitmapSlotMetadata(labelText: String, val numbers: List<Int>, marginRight: Float) :
    SlotMetadata(labelText, marginRight) {}

class TextSlotMetadata(labelText: String, val text: String, marginRight: Float) :
    SlotMetadata(labelText, marginRight) {}