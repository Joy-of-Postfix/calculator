package compose.project.joyturtlegraphics

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// ----------------------App.kt---------------------------------
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview


// ------------------------Platform.kt--------------------------
class JVMPlatform {
    val name: String = "Java ${System.getProperty("java.version")}"
}

fun getPlatform() = JVMPlatform()


// --------------------------App.kt------------------------------
@Composable
@Preview
fun App() {
    var tfstate = TextFieldState()
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface //primaryContainer
                )
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start //.CenterHorizontally,

        ) {
            Row() {
                Button(
                    onClick = { showContent = !showContent }
                ) {
                    Text("CALC")
                }
                Button(onClick = { showContent = !showContent }) {
                    Text("LOAD")
                }
            }
            TextField(state=tfstate,     // Outlined...
                readOnly=false,
                modifier = Modifier.fillMaxWidth(),
                lineLimits = TextFieldLineLimits.MultiLine(5,12), //TextFieldLineLimits.Default,
            )
            Canvas(modifier = Modifier.fillMaxSize()) { onDraw() }
            /*
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) { Image(painterResource(Res.drawable.compose_multiplatform), null)
            }
            */

        }
    }
}

fun DrawScope.onDraw() {
    drawOval(Color.Green)
    drawLine(Color.Blue,Offset(20.0.toFloat(),20.0.toFloat()),
        Offset(250.0.toFloat(),250.0.toFloat()),strokeWidth=4.0.toFloat())
    drawRect(Color.Red,Offset(50.0.toFloat(),20.0.toFloat()),
        Size(100.0.toFloat(),40.0.toFloat()))

}




// --------------------------Main.kt----------------------------
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Joy with Turtle Graphics",
        resizable=true,
    ) {
        App()
    }
}
