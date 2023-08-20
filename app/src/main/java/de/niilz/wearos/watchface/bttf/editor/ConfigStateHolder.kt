package de.niilz.wearos.watchface.bttf.editor

import androidx.activity.ComponentActivity
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.CoroutineScope

class ConfigStateHolder(private val scope: CoroutineScope, activity: ComponentActivity) {

    private lateinit var editorSession: EditorSession

    /*
    val uiState: StateFlow<Int> = flow<Int> {
        editorSession = EditorSession.createOnWatchEditorSession(activity)
        val userSettings = editorSession.userStyleSchema.userStyleSettings
        assert(userSettings.size == 1)
        val complicationSetting = userSettings[0]
        assert(complicationSetting.id.value == ("slot-count"))
        val complicationCount = complicationSetting.options[0]
      emit(complicationCount.)

    }.stateIn(scope + Dispatchers.Main.immediate, SharingStarted.Eagerly, 0)
     */

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
