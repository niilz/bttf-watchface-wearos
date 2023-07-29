package de.niilz.wearos.watchface.bttf.editor

import android.os.Bundle
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

class ComplicationConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            }
        }
    }

    private fun chooseComplication(id: Int) {
        println("Clicked complication button")

        // TODO: have a state holder class
        //state.setComplication(42 + id)
    }
}
