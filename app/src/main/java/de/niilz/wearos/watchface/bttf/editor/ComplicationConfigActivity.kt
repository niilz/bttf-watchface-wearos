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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import de.niilz.wearos.watchface.bttf.TAG
import kotlinx.coroutines.launch
import retrieveSlotCount

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
      val complicationCount = retrieveSlotCount(editorSession.userStyle)
      setContent {
        complicationSelectRow(
          chooseComplication = ::selectComplicationHandler,
          updateComplicationOption = ::updateComplicationOption,
          maxId = complicationCount
        )
      }
    }
  }

  private fun updateComplicationOption(complicationCount: Int) {
    val complicationSettings = editorSession.userStyleSchema.userStyleSettings[0]
    val newOption =
      editorSession.userStyleSchema.userStyleSettings[0].options[complicationCount]
    val mutableSetting = editorSession.userStyle.value.toMutableUserStyle()
    mutableSetting[complicationSettings] = newOption
    editorSession.userStyle.value = mutableSetting.toUserStyle()
    Log.i(TAG, "Updated setting to: ${editorSession.userStyle.value}")
  }

  private fun selectComplicationHandler(id: Int) {
    Log.i(TAG, "Selected Complication, ID: $id")

    // TODO: have a state holder class
    lifecycleScope.launch {
      editorSession.openComplicationDataSourceChooser(100 + id)
    }
  }
}

@Composable
fun complicationSelectRow(
  chooseComplication: (Int) -> Unit,
  updateComplicationOption: (Int) -> Unit,
  maxId: Int
) {
  val (complicationCount, setComplicationCount) = remember { mutableIntStateOf(maxId) }
  val myMod = Modifier
    .background(color = Color.Black)
    .fillMaxWidth()
    .fillMaxSize()

  val handleAddComplication = {
    val newComplicationCount = complicationCount + 1
    setComplicationCount(newComplicationCount)
    updateComplicationOption(newComplicationCount)
  }

  val handleRemoveComplication = {
    val newComplicationCount = complicationCount - 1
    setComplicationCount(newComplicationCount)
    updateComplicationOption(newComplicationCount)
  }

  Column(
    modifier = myMod,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Chose Complications", color = Color.White)
    LazyRow {
      items(complicationCount) { id ->
        complicationChooserButton(
          selectComplicationHandler = chooseComplication,
          removeComplication = handleRemoveComplication,
          id = id
        )
      }
      item {
        Button(onClick = handleAddComplication) {
          Text("+")
        }
      }
    }
  }
}

@Composable
fun complicationChooserButton(
  selectComplicationHandler: (Int) -> Unit,
  removeComplication: () -> Unit,
  id: Int
) {
  val chooseButtonModifier = Modifier.width(50.dp)
  val removeFieldModifier = Modifier
    .width(30.dp)
    .height(30.dp)
  val chooseButtonColor = ButtonDefaults.buttonColors(containerColor = Color.Blue)
  val removeFieldColor = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
  Column(modifier = chooseButtonModifier, horizontalAlignment = Alignment.CenterHorizontally) {
    // Do not use 0 as Id
    val idValue = id + 1
    Button(
      onClick = { selectComplicationHandler(idValue) },
      modifier = chooseButtonModifier,
      colors = chooseButtonColor,
    ) {
      Text("$idValue", textAlign = TextAlign.Center)
    }
    Button(
      onClick = removeComplication,
      modifier = removeFieldModifier,
      colors = removeFieldColor,
      contentPadding = PaddingValues(0.dp)
    ) {
      Text(text = "❌", fontSize = 1.em, textAlign = TextAlign.Center)
    }
  }
}


@Preview(device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
fun preview() {
  var (complicationCount, setComplicationCount) = remember { mutableStateOf(3) }
  val dummyCallback: (Int) -> Unit = { id -> println("The ID is: $id") }
  complicationSelectRow(
    dummyCallback,
    { println("Updated complication option") },
    complicationCount
  )
}
