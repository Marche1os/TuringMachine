import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NewSessionDialog(
    onDismissRequest: () -> Unit,
    onCreateSession: (params: InitParams) -> Unit
) {
    var states by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var initialTape by remember { mutableStateOf("") }
    var headPosition by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = 24.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                TextField(
                    value = states,
                    label = { Text("Количество состояний M") },
                    onValueChange = { newValue ->
                        states = newValue
                    },
                )

                TextField(
                    value = number,
                    label = { Text("Число N") },
                    onValueChange = { newValue ->
                        number = newValue
                    },
                )

                TextField(
                    value = initialTape,
                    label = { Text("Значения ленты") },
                    onValueChange = { newValue ->
                        initialTape = newValue
                    },
                )

                TextField(
                    value = headPosition,
                    label = { Text("Начальная позиция") },
                    onValueChange = { newValue ->
                        headPosition = newValue
                    },
                )

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colors.error)
                }

                Spacer(Modifier.height(16.dp))

                Row {
                    TextButton(onClick = onDismissRequest) {
                        Text("Отменить")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        errorMessage = ""

                        val countOfStates = states.toIntOrNull()
                        if (countOfStates == null || countOfStates < 0) {
                            errorMessage = "Число состояний M должно быть целым от 0 до M "
                            return@Button
                        }

                        val alphabet = number.toIntOrNull()

                        if (alphabet == null || alphabet < 0) {
                            errorMessage = "Число N должно быть целым от 0 до N"
                            return@Button
                        }

                        val tape = initialTape.chunked(1).map { it.toCharArray().first() }
                        val head = headPosition.toIntOrNull()

                        if (head == null || head < 0) {
                            errorMessage = "Начальная позиция должна быть не меньше 0"
                            return@Button
                        }

                        onCreateSession(
                            InitParams(
                                countOfStates = countOfStates,
                                numbersOfAlphabet = alphabet,
                                tape = tape,
                                headPosition = head,
                            )
                        )

                        onDismissRequest()

                    }) {
                        Text("Подтвердить")
                    }
                }
            }
        }
    }
}
