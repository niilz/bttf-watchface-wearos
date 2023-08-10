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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        /*
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            state.complicationData.collect { complicationData ->
                println("### collected complication data")
            }
        }
         */

        val myMod = Modifier
            .background(color = Color.Black)
            .fillMaxWidth()
            .fillMaxSize()
        setContent {
            Column(
                modifier = myMod,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("TODO:", color = Color.White)
                Text("Settings Activity", color = Color.White)
                Button(onClick = { chooseComplication(0) }) {
                }
                Button(onClick = { chooseComplication(1) }) {
                }
                Button(onClick = { chooseComplication(2) }) {
                }
                Button(onClick = { chooseComplication(3) }) {
                }
            }
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
