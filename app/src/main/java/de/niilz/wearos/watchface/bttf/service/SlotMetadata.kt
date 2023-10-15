package de.niilz.wearos.watchface.bttf.service

import java.time.ZonedDateTime

class SlotMetadata(
  val labelText: String? = null,
  val valueColor: Int,
  val slotValues: List<SlotValue>,
  val now: ZonedDateTime? = null,
) {}

open class SlotValue()

data class NumVal(val num: Int) : SlotValue()

data class TextVal(val text: String) : SlotValue()

class ShapeVal : SlotValue()