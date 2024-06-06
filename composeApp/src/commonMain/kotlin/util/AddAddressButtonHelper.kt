package util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AddAddressButtonHelper(
    onShowBottomSheet: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 16.dp)
        ,
        //.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier,
            //.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Card(
                modifier = Modifier
                    //.fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(100.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onShowBottomSheet(true)
//                            isShowBottomSheet.value = true
                        }
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_plus),
                        contentDescription = null,
                        tint = ColorCustomResources.colorBazaMainBlue
                    )
                    Text(
                        modifier = Modifier
                            //.fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp),
                        text = "Новый адрес",
                        color = ColorCustomResources.colorBazaMainBlue
                    )
                }
            }
        }
    }
}