package de.niilz.wearos.watchface.bttf.service

class SlotMetadata(
    val labelText: String,
    val valueColor: Int,
    val marginRight: Float,
    vararg val slotValues: SlotValue
) {}

open class SlotValue()

data class NumVal(val numbers: List<Int>) : SlotValue()

data class TextVal(val text: String) : SlotValue()
