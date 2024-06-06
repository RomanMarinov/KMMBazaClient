package presentation.ui.profile_screen.address_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import domain.model.user_info.AdditionalAddresses
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_account
import kmm.composeapp.generated.resources.ic_add_address
import kmm.composeapp.generated.resources.ic_basket
import kmm.composeapp.generated.resources.ic_home
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.profile_screen.address_screen.utils.VerifyStatus
import util.ColorCustomResources
import util.ProgressBarHelper
import util.ShimmerListOutdoorOrDomofonHelper
import util.SnackBarHostHelper


enum class SnackBarAddressDeleted {
    SUCCESS_DELETED, FAILURE_DELETED, DEFAULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressContentWithRefresh(
    additionalAddresses: List<AdditionalAddresses>,
    onRefresh: Any,
    navHostController: NavHostController,
    onMoveToAuthActivity: () -> Unit,
    viewModel: AddressesScreenViewModel
) {

    val addressDeleted by viewModel.addressDeleted.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    var isLoadingState by remember { mutableStateOf(true) }
    val isShowSnackBarAddressDeleted = remember { mutableStateOf(SnackBarAddressDeleted.DEFAULT) }
    val isShowProgressBarAddressDelete = remember { mutableStateOf(false) }

    LaunchedEffect(additionalAddresses) {
        //  isShowProgressBarAddressDelete.value = false // не проверил но долно быть уместно
        if (additionalAddresses.isNotEmpty()) {
            isLoadingState = false
        }
    }

    LaunchedEffect(addressDeleted) {
        addressDeleted?.let {
            if (it) {
                isShowProgressBarAddressDelete.value = false
                isShowSnackBarAddressDeleted.value = SnackBarAddressDeleted.SUCCESS_DELETED
                viewModel.getUserInfo() // обновление списка
            } else {
                isShowProgressBarAddressDelete.value = false
                isShowSnackBarAddressDeleted.value = SnackBarAddressDeleted.FAILURE_DELETED
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                //.padding(bottom = 16.dp)
            ) {
                addressesContentList(
                    additionalAddresses = additionalAddresses,
                    isLoading = isLoadingState,
                    viewModel = viewModel,
                    onShowProgressBarAddressDelete = {
                        isShowProgressBarAddressDelete.value = it
                    },
                    addressDeleted = addressDeleted
                )
            }
        }

        if (isShowProgressBarAddressDelete.value) {
            ProgressBarHelper.Start(
                trackColor = Color.Transparent,
                mainColor = ColorCustomResources.colorBazaMainBlue
            )
        }

        if (isShowSnackBarAddressDeleted.value == SnackBarAddressDeleted.SUCCESS_DELETED) {
            SnackBarHostHelper.ShortShortTime(
                message = "Адрес успешно удален",
                onFinishTime = {
                    isShowSnackBarAddressDeleted.value = SnackBarAddressDeleted.DEFAULT
                }
            )
        }
        if (isShowSnackBarAddressDeleted.value == SnackBarAddressDeleted.FAILURE_DELETED) {
            SnackBarHostHelper.ShortShortTime(
                message = "Не удалось удалить адрес",
                onFinishTime = {
                    isShowSnackBarAddressDeleted.value = SnackBarAddressDeleted.DEFAULT
                }
            )
        }
    }
}


fun LazyListScope.addressesContentList(
    additionalAddresses: List<AdditionalAddresses>,
    isLoading: Boolean,
    viewModel: AddressesScreenViewModel,
    onShowProgressBarAddressDelete: (Boolean) -> Unit,
    addressDeleted: Boolean?
) {
    items(additionalAddresses) { address ->
        var contentExpanded by remember { mutableStateOf(false) }
        LaunchedEffect(addressDeleted) {
            addressDeleted?.let {
                contentExpanded = addressDeleted == false
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val verifyStatus = VerifyStatus.getStatus(additionalAddresses = address)

        ShimmerListOutdoorOrDomofonHelper(
            isLoading = isLoading,
            contentAfterLoading = {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    contentExpanded = !contentExpanded
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.ic_home),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(34.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Row {
                                    Text(
                                        text = "${address.city}, ${address.street} дом ${address.home}, кв. ${address.flat}",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(2f) // или 1ф
                                    )
                                }

                                Row {
                                    Text(
                                        text = verifyStatus,
                                        color = Color.Gray
                                    )
                                }
                            }

                            IconButton(
                                onClick = { contentExpanded = !contentExpanded }) {
                                Icon(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .weight(1f)
                                        .width(IntrinsicSize.Max),
                                    imageVector = if (contentExpanded) {
                                        Icons.Filled.KeyboardArrowUp
                                    } else {
                                        Icons.Filled.KeyboardArrowDown
                                    },
                                    contentDescription = if (contentExpanded) {
                                        "ExpandLess"
                                    } else {
                                        "ExpandMore"
                                    }
                                )
                            }
                        }

                        if (contentExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Divide()
                                TitleExpand()
                                val fakeList = listOf(
                                    FakeUserShared(name = "Эминем", phone = "+79303493563"),
                                    FakeUserShared(name = "Зинка со школы", phone = "+79303333333")
                                )
                                fakeList.forEachIndexed { index, user ->
                                    UserSharedItem(index = index, fakeList = fakeList, user = user)
                                }
                                ShareAddress()
                                Divide()
                                DeleteAddress(
                                    additionalAddress = address,
                                    viewModel = viewModel,
                                    onExpandedContent = {
                                        //contentExpanded = false
                                    },
                                    onShowProgressBarAddressDelete = {
                                        onShowProgressBarAddressDelete(it)
                                    }
                                )
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@Composable
fun Divide() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .height(1.dp)
            .background(Color.LightGray)
    )
}

@Composable
fun TitleExpand() {
    Text(
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp
        ),
        text = "Пользователи, которым Вы предоставили доступ к адресу:",
        color = Color.Black
    )
}

data class FakeUserShared(val name: String, val phone: String)

@Composable
fun UserSharedItem(index: Int, fakeList: List<FakeUserShared>, user: FakeUserShared) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Ensure the column takes the full width

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_account),
                contentDescription = "back",
                tint = Color.Gray,
                modifier = Modifier
                    .padding(8.dp)
                    .size(34.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Row { Text(text = user.name) }
                Row { Text(text = user.phone) }
            }

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(34.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable { },
                imageVector = vectorResource(Res.drawable.ic_basket),
                contentDescription = "back",
                tint = Color.Gray,
            )
        }
        Divide()
    }
}

@Composable
fun ShareAddress() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            },
        //    .padding(16.dp)
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Поделиться адресом",
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_add_address),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun DeleteAddress(
    additionalAddress: AdditionalAddresses,
    viewModel: AddressesScreenViewModel,
    onExpandedContent: (Boolean) -> Unit,
    onShowProgressBarAddressDelete: (Boolean) -> Unit
) {
    val isShowDialogDeleteAddress = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isShowDialogDeleteAddress.value = true
            }
            .padding(top = 16.dp, end = 12.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Удалить адрес",
            color = Color.Black
        )
    }

    if (isShowDialogDeleteAddress.value) {
        Dialog(
            onPositiveClick = {
                onShowProgressBarAddressDelete(true)
                viewModel.deleteAddress(id = additionalAddress.id)
                // onExpandedContent(false)
                isShowDialogDeleteAddress.value = false
            },
            onNegativeClick = {
                isShowDialogDeleteAddress.value = false
            },
            additionalAddress = additionalAddress
        )
    }
}

@Composable
fun Dialog(
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    additionalAddress: AdditionalAddresses,
) {
    Box {
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
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Удалить адрес?",
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "${additionalAddress.city}, ${additionalAddress.street} дом ${additionalAddress.home}, кв. ${additionalAddress.flat}",
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
                            content = { Text(text = "ОК") },
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
