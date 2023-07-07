package de.niilz.wearos.watchface.bttf

open class SlotMetadata(
    val labelText: String,
    val valueColor: Int,
    val marginRight: Float,
) {}

class BitmapSlotMetadata(
    labelText: String,
    val numbers: List<Int>,
    valueColor: Int,
    marginRight: Float
) :
    SlotMetadata(labelText, valueColor, marginRight) {}

class TextSlotMetadata(labelText: String, val text: String, valueColor: Int, marginRight: Float) :
    SlotMetadata(labelText, valueColor, marginRight) {}