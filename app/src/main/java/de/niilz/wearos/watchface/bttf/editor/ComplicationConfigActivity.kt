package de.niilz.wearos.watchface.bttf.editor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import de.niilz.wearos.watchface.bttf.TAG
import kotlinx.coroutines.launch

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

        setContent {
            complicationSelectRow(chooseComplication = ::selectComplicationHandler, ids = listOf(0, 1, 2, 3))
        }
    }

    private fun selectComplicationHandler(id: Int) {
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
        LazyRow {
            items(ids) { id ->
                complicationChooserButton(selectComplicationHandler = chooseComplication, id = id)
            }
        }
    }
}

@Composable
fun complicationChooserButton(selectComplicationHandler: (Int) -> Unit, id: Int) {
    val buttonModifier = Modifier.width(50.dp)
    val buttonColors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    Button(onClick = { selectComplicationHandler(id) }, modifier = buttonModifier, colors = buttonColors) {}
}


@Preview(device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
fun preview() {
    val dummyCallback: (Int) -> Unit = { id -> println("The ID is: $id") }
    complicationSelectRow(dummyCallback, listOf(0, 1, 2, 3, 4, 5, 6))
}
