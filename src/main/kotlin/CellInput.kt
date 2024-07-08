import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
fun CustomTextField(
    countOfStates: Int,
    onTextChangedCallback: (newText: String) -> Unit,
) {
    // Состояние для хранения введенного текста
    var text by remember { mutableStateOf("") }

    val numberRegex = if (countOfStates < 10) {
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
            1 -> parts[0].matches("\\d?".toRegex())
            2 -> parts[0].matches("\\d?".toRegex()) &&
                    parts[1].matches("[LRS]?".toRegex())
            3 -> parts[0].matches("\\d".toRegex()) &&
                    parts[1].matches("[LRS]".toRegex()) &&
                    parts[2].matches("Q$numberRegex".toRegex())
            else -> false
        }

    }

    // Обработка изменения текста
    fun onTextChanged(newText: String) {
        if (newText.isEmpty() || isValidInput(newText)) {
            text = newText
            onTextChangedCallback(text)
        }
    }

    // Поле ввода с обработкой ввода
    OutlinedTextField(
        value = text,
        onValueChange = { newText -> onTextChanged(newText) },
        label = { Text("Input") },
        placeholder = { Text("e.g., 2 L Q3") }
    )
}
