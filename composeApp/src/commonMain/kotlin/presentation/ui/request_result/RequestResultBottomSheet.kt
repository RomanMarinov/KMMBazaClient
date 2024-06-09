package presentation.ui.request_result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.failed_to_send_service_request
import kmm.composeapp.generated.resources.ic_close
import kmm.composeapp.generated.resources.try_one_more_time_later
import kmm.composeapp.generated.resources.wait_request_result_details
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import util.ColorCustomResources
import util.Constants
import util.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestResultBottomSheet(
    ticketId: Int,
    navigationFrom: String,
    onCloseAllBottomSheet: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = {
            onCloseAllBottomSheet()
        },
        sheetState = bottomSheetState,
        dragHandle = { },
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            RequestResultBottomSheetContent(
                ticketId = ticketId,
                navigationFrom = navigationFrom,
                onCloseAllBottomSheet = {
                    onCloseAllBottomSheet()
                }
            )
        }
    }

}

@Composable
fun RequestResultBottomSheetContent(
    ticketId: Int,
    navigationFrom: String,
    onCloseAllBottomSheet: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                            onCloseAllBottomSheet()
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

    if (ticketId > Constants.RequestResultTicketId.DEFAULT_INT) {
        SuccessRequestResult(
            ticketId = ticketId,
            navigationFrom = navigationFrom
        )
    } else {
        FailureRequestResult()
    }
}

@Composable
fun SuccessRequestResult(
    ticketId: Int,
    navigationFrom: String
) {
    var description = ""

    when (navigationFrom) {
        ScreenRoute.DomofonScreen.route,
        ScreenRoute.ProfileScreen.route,
        ScreenRoute.OutdoorScreen.route -> {
            description = stringResource(Res.string.wait_request_result_details)
        }
//        net.baza.bazanetclientapp.Constants.FromFragmentMarkers.FROM_DVR_REQUEST_PHONE -> {
//            showViewsInCommonCase(detail = resources.getString(R.string.dvr_request_result_details))
//        }

//        net.baza.bazanetclientapp.Constants.FromFragmentMarkers.FROM_BAZA,
//        net.baza.bazanetclientapp.Constants.FromFragmentMarkers.FROM_CHANGE_TARIFF,
//        net.baza.bazanetclientapp.Constants.FromFragmentMarkers.FROM_DOMOFON_REQUEST_PHONE -> {
//            showViewsInCommonCase(detail = resources.getString(R.string.service_request_result_details))
//        }
//
//
//        net.baza.bazanetclientapp.Constants.FromFragmentMarkers.FROM_MOBILE_REQUEST -> {
//            showViewsInCommonCase(detail = resources.getString(R.string.mobile_request_result_details))
//        }
//
//
//        net.baza.bazanetclientapp.Constants.FromFragmentMarkers.FROM_STATIC_IP_REQUEST -> {
//            showViewsInCommonCase(detail = resources.getString(R.string.wait_static_ip_request_result_details))
//        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Text(
            text = "Заявка №$ticketId",
            fontWeight = FontWeight.Bold
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = description
        )
    }
}

@Composable
fun FailureRequestResult() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.failed_to_send_service_request)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = stringResource(Res.string.try_one_more_time_later)
        )
    }
}