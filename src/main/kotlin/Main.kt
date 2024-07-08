import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {

    MaterialTheme() {
        TuringMachineUI()
    }
}

@Composable
fun TuringMachineUI() {
    var showNewSessionDialog by remember { mutableStateOf(false) }
    var initParams by remember { mutableStateOf<InitParams?>(null) }
    val headPosition = remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.safeContentPadding()
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                onClick = { showNewSessionDialog = true }) {
                Text(text = "Новая сессия")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                onClick = {}) {
                Text(text = "Выполнить шаг")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                onClick = {}) {
                Text(text = "Запустить выполнение")
            }

        }

        PopupMenu(
            onNewSessionClick = { showNewSessionDialog = true },
            onOneStepRunClick = {},
            onRunLoopClick = {},
            onStopClick = {},
        )

        if (showNewSessionDialog) {
            NewSessionDialog(
                onDismissRequest = { showNewSessionDialog = false },
                onCreateSession = { params -> initParams = params }
            )
        }

        initParams?.let { params ->
            headPosition.value = params.headPosition
            TapeUI(
                TapeData(params.tape.toMutableList()),
                headPosition,
            )

            Row {
                IconButton(
                    onClick = {
                        if (headPosition.value > 0) headPosition.value--
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }

                Spacer(Modifier.width(12.dp))

                IconButton(
                    onClick = { headPosition.value++ },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null,
                    )
                }

            }

            TransitionTable(
                stateCount = params.countOfStates,
                symbolCount = params.numbersOfAlphabet,
            )

        }

    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Эмулятор машины Тьюринга",
    ) {
        App()
    }
}
