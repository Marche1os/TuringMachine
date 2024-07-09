import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@Composable
@Preview
fun App() {

    MaterialTheme() {
        MainUI()
    }
}

@Composable
fun MainUI() {
    val stateFlow = remember { MutableStateFlow<State>(State.NotInit) }

    var countOfNumbers by remember { mutableStateOf(0) }
    var countOfStates by remember { mutableStateOf(0) }
    var tape by remember { mutableStateOf(List(10) { " " }) }
    var headPosition by remember { mutableStateOf(0) }
    var transitionTable by remember {
        mutableStateOf(Array(countOfNumbers) {
            Array(countOfStates) {
                Triple(
                    "",
                    "",
                    ""
                )
            }
        })
    }
    var currentState by remember { mutableStateOf(0) }
    var running by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showNewSessionDialog by remember { mutableStateOf(false) }

    val initMachine: (newN: Int, newM: Int, newTape: List<String>, startHead: Int) -> Unit =
        { newN, newM, newTape, startHead ->
            countOfNumbers = newN
            countOfStates = newM
            tape = newTape + List(10_000) { " " }
            headPosition = startHead
            transitionTable = Array(countOfNumbers) { Array(countOfStates) { Triple("", "", "") } }
            currentState = 1
            running = false
        }

    if (showNewSessionDialog) {
        NewSessionDialog(
            onDismissRequest = { showNewSessionDialog = false },
            onCreateSession = { params ->
                initMachine(params.numbersOfAlphabet, params.countOfStates, params.tape, params.headPosition)
                stateFlow.tryEmit(State.NewSession)
            }
        )
    }

    fun performStep() {
        val currentSymbol = tape[headPosition].toIntOrNull()

        if (currentSymbol == null) {
            running = false
            return
        }

        val (write, move, nextState) = transitionTable[currentSymbol][currentState - 1]

        if (write.isEmpty() || move.isEmpty() || nextState.isEmpty()) {
            return
        }

        tape = tape.toMutableList().apply { this[headPosition] = write }

        if (move == "R") {
            headPosition++
        } else if (move == "L" && headPosition > 0) {
            headPosition--
        }

        if (nextState.removePrefix("Q") == "0") {
            running = false
            return
        }

        currentState = nextState
            .removePrefix("Q")
            .toInt()
    }

    fun runMachine(pauseSeconds: Float) {
        coroutineScope.launch {
            running = true
            while (running) {
                performStep()
                delay((pauseSeconds * 1000).toInt().toLong())
            }
        }
    }

    fun stopMachine() {
        running = false
    }

    when (stateFlow.collectAsState().value) {
        State.NotInit -> EmptyContentUI { showNewSessionDialog = true }
        State.NewSession -> {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = {
                            showNewSessionDialog = true
                            stateFlow.tryEmit(State.NotInit)
                        }) {
                        Text("Начать новый сеанс")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = { performStep() }) {
                        Text("Шаг")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = { runMachine(1f) }) {
                        Text("Запуск")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        onClick = { stopMachine() }) {
                        Text("Остановить")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Лента", fontSize = 20.sp)
                LazyRow {
                    items(tape.size) { index ->
                        Box(
                            modifier = Modifier.padding(8.dp).size(40.dp)
                                .border(1.dp, if (index == headPosition) Color.Green else Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tape[index])
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Таблица переходов", fontSize = 20.sp)

                val states = remember { List(countOfStates) { index -> "Q${index + 1}" } }
                val symbols = remember { List(countOfNumbers) { index -> index.toString() } }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(countOfStates + 1),
                    modifier = Modifier.padding(16.dp),
                ) {
                    // First row (header of columns)
                    item { /* Empty top-left cell */ }
                    states.forEach { state ->
                        item {
                            HeaderCell(text = state)
                        }
                    }

                    // Remaining rows (each symbol row)
                    symbols.forEachIndexed { symbolInd, symbol ->
                        item {
                            HeaderCell(text = symbol)
                        }

                        states.forEachIndexed { ind, _ ->
                            item {
                                Cell(countOfStates,
                                    (0..<countOfNumbers),
                                    { (a, b, c) ->
                                        transitionTable[symbolInd][ind] = Triple(a, b, c)
                                    })
                            }
                        }
                    }
                }
            }

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
