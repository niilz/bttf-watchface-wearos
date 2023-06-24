package de.niilz.wearos.watchface.bttf

open class SlotMetadata(
    val labelText: String,
) {}

class BitmapSlotMetadata(labelText: String, val numbers: List<Int>) :
    SlotMetadata(labelText) {}

class TextSlotMetadata(labelText: String, val text: String) :
    SlotMetadata(labelText) {}