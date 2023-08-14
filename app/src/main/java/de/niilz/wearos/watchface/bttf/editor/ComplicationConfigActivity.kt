package de.niilz.wearos.watchface.bttf.editor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import de.niilz.wearos.watchface.bttf.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ComplicationConfigActivity : ComponentActivity() {

    private lateinit var editorSession: EditorSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            editorSession =
                EditorSession.createOnWatchEditorSession(this@ComplicationConfigActivity)

            val hasHeartRatePermission =
                checkSelfPermission(Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED
            if (!hasHeartRatePermission) {
                requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), 1)
            }
        }
        /*
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            state.complicationData.collect { complicationData ->
                println("### collected complication data")
            }
        }
         */

        setContent {
            complicationSelectRow(chooseComplication = ::chooseComplication, ids = listOf(0, 1, 2, 3))
        }
    }

    private fun chooseComplication(id: Int) {
        Log.i(TAG, "Selected Complication, ID: $id")

        // TODO: have a state holder class
        lifecycleScope.launch {
            editorSession.openComplicationDataSourceChooser(42 + id)
        }
    }
}

@Composable
fun complicationSelectRow(chooseComplication: (Int) -> Unit, ids: List<Int>) {
    val myMod = Modifier
        .background(color = Color.Black)
        .fillMaxWidth()
        .fillMaxSize()

    Column(
        modifier = myMod,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Chose Complications", color = Color.White)
        Row {
            for (id in ids) {
                complicationChooserButton(selectComplicationHandler = chooseComplication, id = id)
            }
        }
    }
}

@Composable
fun complicationChooserButton(selectComplicationHandler: (Int) -> Unit, id: Int) {
    val buttonColors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    Button(onClick = { selectComplicationHandler(id) }, colors = buttonColors) {}
}


@Preview(device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
fun preview() {
    val dummyCallback: (Int) -> Unit = { id -> println("The ID is: $id") }
    complicationSelectRow(dummyCallback, listOf(0, 1, 2, 3))
}
