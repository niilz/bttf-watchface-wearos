package de.niilz.wearos.watchface.bttf

class SlotMetadata(val labelText: String, val text: String?, val numbers: List<Int>?) {
    constructor(labelText: String, text: String) : this(labelText, text, null)
    constructor(labelText: String, numbers: List<Int>) : this(labelText, null, numbers)
}