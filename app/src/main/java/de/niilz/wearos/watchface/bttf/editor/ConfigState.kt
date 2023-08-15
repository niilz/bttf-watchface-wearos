package de.niilz.wearos.watchface.bttf.editor

class ConfigState {
  companion object {
    var complicationCount = 0

    fun incrementComplicationCount() {
      complicationCount += 1
    }

    fun decrementComplicationCount() {
      complicationCount -= 1
    }
  }
}
