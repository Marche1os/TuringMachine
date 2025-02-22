import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun CustomTextField(
    countOfStates: Int,
    alphabet: Int,
    isTerminated: Boolean,
    onTextChangedCallback: (Triple<String, String, String>) -> Unit,
) {
    // Состояние для хранения введенного текста
    var text by rememberSaveable { mutableStateOf("") }

    val numbersRegex = if (alphabet < 10) {
        "[0-$alphabet]?"
    } else {
        "[0-9]?"
    }

    val statesRegex = if (countOfStates < 10) {
        "[0-$countOfStates]?"
    } else {
        "(10|[0-$countOfStates])?"
    }


    // Функция для проверки допустимости текущего ввода
    fun isValidInput(input: String): Boolean {
        if (input.last() == ' ') return true

        val parts = input.split(" ")

        // Проверяем каждую часть по мере ввода
        return when (parts.size) {
            1 -> parts[0].matches(numbersRegex.toRegex())
            2 -> parts[0].matches(numbersRegex.toRegex()) &&
                    parts[1].matches("[LRS]?".toRegex())

            3 -> parts[0].matches(numbersRegex.toRegex()) &&
                    parts[1].matches("[LRS]".toRegex()) &&
                    parts[2].matches("Q$statesRegex".toRegex())

            else -> false
        }

    }

    // Обработка изменения текста
    fun onTextChanged(newText: String) {
        if (newText.isEmpty() || isValidInput(newText)) {
            text = newText
            val textArray = text.split(" ")
            if (text.matches("\\d [LRS] Q\\d".toRegex())) {
                val output = Triple(textArray.first(), textArray[1], textArray.last())
                onTextChangedCallback(output)
            }
        }
    }

    // Поле ввода с обработкой ввода
    OutlinedTextField(
        value = text,
        onValueChange = { newText -> onTextChanged(newText) },
        enabled = !isTerminated,
        label = { Text("Input") },
        placeholder = { Text("e.g., 2 L Q3") }
    )
}
