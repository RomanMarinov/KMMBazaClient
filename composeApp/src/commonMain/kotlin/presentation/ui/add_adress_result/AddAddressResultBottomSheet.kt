package presentation.ui.add_adress_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_close
import kmm.composeapp.generated.resources.image_dino_bad
import kmm.composeapp.generated.resources.image_dino_cool
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.add_adress_result.model.ResultSendPhoto
import util.ColorCustomResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressResultBottomSheet(
    resultSendPhoto: ResultSendPhoto,
    onShowBottomSheet: (Boolean) -> Unit,
) {
    val openBottomSheetState by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
//            containerColor = Color.White, не
//            contentColor = Color.White,
        onDismissRequest = { onShowBottomSheet(false) },
        sheetState = bottomSheetState,
        dragHandle = { },
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .padding(PaddingValues().calculateBottomPadding())
            ) {
                ResultContent(
                    resultSendPhoto = resultSendPhoto,
                    onShowBottomSheet = {
                        onShowBottomSheet(it)
                    }
                )
            }
        }
    }
}

@Composable
fun ResultContent(
    resultSendPhoto: ResultSendPhoto,
    onShowBottomSheet: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Добавление адреса",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Card(
                modifier = Modifier
                    .size(34.dp),
                //    .align(Alignment.CenterEnd)
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onShowBottomSheet(false)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        // close
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_close),
                        contentDescription = null,
                    )
                }
            }
        }
    }

    val titleDino: String = getTitle(resultSendPhoto = resultSendPhoto)

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = titleDino
        )
    }

    val imageDino: Painter = getImage(resultSendPhoto = resultSendPhoto)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = imageDino,
            modifier = Modifier
                .size(356.dp, 283.dp),
            contentDescription = null
        )
    }
}

@Composable
private fun getImage(resultSendPhoto: ResultSendPhoto) : Painter {
    return when(resultSendPhoto) {
        ResultSendPhoto.SUCCESS -> {
            painterResource(Res.drawable.image_dino_cool)
        }
        ResultSendPhoto.FAILURE -> {
            painterResource(Res.drawable.image_dino_bad)
        }

        ResultSendPhoto.DEFAULT -> {
            ColorPainter(Color.Transparent)
        }
    }
}

private fun getTitle(resultSendPhoto: ResultSendPhoto) : String {
    return when(resultSendPhoto) {
        ResultSendPhoto.SUCCESS -> {
            "Мы получили фото документа. Для подтверждения адреса нам потребуется время на проверку."
        }
        ResultSendPhoto.FAILURE -> {
           "К сожалению, фотографию не удалось отправить. Пожалуйста, попробуйте еще раз."
        }

        ResultSendPhoto.DEFAULT -> {
            ""
        }
    }
}