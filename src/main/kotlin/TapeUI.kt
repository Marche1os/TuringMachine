import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TapeUI(
    tapeData: TapeData,
    initialHeadPosition: MutableIntState,
) {
    val wholeTape = tapeData.data + List(10_000) { ' ' }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(wholeTape) { index, symbol ->
            val color = if (initialHeadPosition.value == index) {
                Color.Green
            } else {
                Color.LightGray
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol.toString(),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

}