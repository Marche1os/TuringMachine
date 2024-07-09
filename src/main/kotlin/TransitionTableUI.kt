import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderCell(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.Gray)
            .padding(8.dp)
            .background(Color.Gray)
            .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun Cell(
    numberOfStates: Int,
    alphabet: Int,
    isTerminated: Boolean,
    onTextChanged: (Triple<String, String, String>) -> Unit
) {
    Box(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colors.onSurface)
            .padding(8.dp)
            .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        CustomTextField(numberOfStates, alphabet, isTerminated, onTextChanged)
    }
}