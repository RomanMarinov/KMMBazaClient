package presentation.ui.attach_photo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_permission
import org.jetbrains.compose.resources.vectorResource
import util.ColorCustomResources

@Composable
fun ImageSourceOptionDialog(
    onDismissRequest: () -> Unit,
    onGalleryRequest: () -> Unit = {},
    onCameraRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Выберите источник получения изображения",
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ElevatedButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                onCameraRequest.invoke()
                            },
                            content = { Text("Камера") },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = ColorCustomResources.colorBazaMainBlue
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ElevatedButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                onGalleryRequest.invoke()
                            },
                            content = { Text("Галерея") },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = ColorCustomResources.colorBazaMainBlue
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlertMessageDialog(
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = {
            onNegativeClick()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(size = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                resource?.let {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = vectorResource(Res.drawable.ic_permission),
                    contentDescription = "",
                    tint = ColorCustomResources.colorBazaMainBlue
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Требуется разрешение",
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Чтобы установить изображение, предоставьте это разрешение. Вы можете управлять разрешениями в настройках вашего устройства.",
                    // fontWeight = FontWeight.Medium,
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    ElevatedButton(
                        modifier = Modifier
                            .background(Color.White)
                            .weight(1f)
                            .padding(end = 8.dp),
                        border = BorderStroke(1.dp, ColorCustomResources.colorBazaMainBlue),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            onNegativeClick()
                        },
                        content = {
                            Text(
                                text = "Отмена",
                                color = Color.Black
                            )
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)

                    )

                    ElevatedButton(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            onPositiveClick()
                        },
                        content = { Text(text = "Настройка") },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = ColorCustomResources.colorBazaMainBlue
                        )
                    )
                }
            }
        }
    }
}