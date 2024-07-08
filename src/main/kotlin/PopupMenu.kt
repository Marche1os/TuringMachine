import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.onClick
import androidx.compose.material.CursorDropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopupMenu(
    onNewSessionClick: () -> Unit,
    onOneStepRunClick: () -> Unit,
    onRunLoopClick: () -> Unit,
    onStopClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        Modifier
            .wrapContentSize()
            .background(Color.Gray)
            .padding(8.dp)
            .onClick {
                expanded = true
            }
    ) {
        Text(text = "Меню")
    }

    var renderCount by remember { mutableStateOf(0) }
    listOf(renderCount, renderCount - 1).forEach { renderId ->
        val isActive = renderId == renderCount
        key(renderId) {
            CursorDropdownMenu(
                expanded = expanded && isActive,
                onDismissRequest = {
                    if (isActive) {
                        renderCount += 1
                        expanded = false
                    }
                },
            ) {
                DropdownMenuItem({
                    expanded = false
                    onNewSessionClick()
                }) {
                    Text("Новый сеанс")
                }

                DropdownMenuItem({ onOneStepRunClick() }) {
                    Text("Запустить на 1 шаг")
                }

                DropdownMenuItem({ onRunLoopClick() }) {
                    Text("Запустить на бесконечное выполнение")
                }

                DropdownMenuItem({ onStopClick() }) {
                    Text("Остановить")
                }
            }
        }
    }
}