package de.niilz.wearos.watchface.bttf

class SlotMetadata(
    val labelText: String,
    val marginRight: Float,
    val text: String?,
    val numbers: List<Int>?
) {
    constructor(labelText: String, marginRight: Float, text: String) : this(
        labelText,
        marginRight,
        text,
        null
    )

    constructor(labelText: String, marginRight: Float, numbers: List<Int>) : this(
        labelText,
        marginRight,
        null,
        numbers
    )
}