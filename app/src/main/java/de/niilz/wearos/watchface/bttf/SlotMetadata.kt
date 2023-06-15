package de.niilz.wearos.watchface.bttf

open class SlotMetadata(
    val labelText: String,
    val marginRight: Float,
) {}

class BitmapSlotMetadata(labelText: String, marginRight: Float, val numbers: List<Int>) :
    SlotMetadata(labelText, marginRight) {}

class TextSlotMetadata(labelText: String, marginRight: Float, val text: String) :
    SlotMetadata(labelText, marginRight) {}