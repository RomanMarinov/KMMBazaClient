package presentation.ui.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.multiplatform.webview.web.LoadingState
import domain.model.user_info.UserInfo
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_addresses
import kmm.composeapp.generated.resources.ic_arrow_right
import kmm.composeapp.generated.resources.ic_chat_with_uk
import kmm.composeapp.generated.resources.ic_devices
import kmm.composeapp.generated.resources.ic_log_out
import kmm.composeapp.generated.resources.ic_payment_card
import kmm.composeapp.generated.resources.ic_person_account
import kmm.composeapp.generated.resources.ic_phone
import kmm.composeapp.generated.resources.ic_profile_card
import kmm.composeapp.generated.resources.ic_profile_post_card
import kmm.composeapp.generated.resources.ic_profile_setting
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import presentation.ui.auth_activity.AuthPlatform
import presentation.ui.profile_screen.email_item.EmailBottomSheet
import presentation.ui.profile_screen.phone_number_item.PhoneNumberBottomSheet
import util.ColorCustomResources
import util.ScreenRoute
import util.TextUtils

//class  ProfileScreenViewModelProvider : KoinComponent {
//    val profileScreenViewModel: ProfileScreenViewModel by inject()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContentWithRefresh(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    navHostController: NavHostController,
    onMoveToAuthActivity: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel
) {

//    val viewModelProvider = ProfileScreenViewModelProvider()
//    val viewModel = viewModelProvider.profileScreenViewModel
//    val viewModel = koinViewModel<ProfileScreenViewModel>()
    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    // var isLoading = remember { mutableStateOf(true) }

    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val logout by viewModel.logout.collectAsStateWithLifecycle()
    logout?.let {
        if (it) {
            // удалить fingerPrint из стор
            viewModel.clearDataStore()
            Logger.d("4444 LogoutPlatform().Logout()")
            onMoveToAuthActivity()
            // LogoutPlatform().Logout()
        } else {
            Logger.d("4444 не получается lgout")
        }
    }

//    LaunchedEffect(moveToAuthScreen) {
//        if (moveToAuthScreen != null && moveToAuthScreen == true) {
//            // deeplink on the auth screen
//            val uri = "scheme_chatalyze://calls_screen".toUri()
//            val deepLink = Intent(Intent.ACTION_VIEW, uri)
//            val pendingIntent: PendingIntent =
//                TaskStackBuilder.create(context).run {
//                    addNextIntentWithParentStack(deepLink)
//                    getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                    )
//                }
//            pendingIntent.send()
//        }
//    }

    Box(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
        // .navigationBarsPadding()
        // .padding(bottom = paddingValue.calculateBottomPadding())
//            .background(
//                Brush.linearGradient(
//                    colors = colorsList,
//                    start = Offset.Zero,
//                    end = Offset.Infinite
//                )
//            )
    ) {


        var phone = ""
        userInfo?.data?.profile?.phone?.let {
            if (it != 0L) {
                phone = TextUtils.getPhoneAsFormattedString(it)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(bottom = 16.dp)
//                .padding(
//                    bottom = paddingValue.calculateBottomPadding()
//                )
        ) {
            profileNameCard(
                userInfo = userInfo,
                viewModel = viewModel
            )

            profilePhoneNumberCard(
                phone = phone
                //openBottomSheet = {
                //openBottomSheetOrderState.value = it
                //}
            )
            profilePersonAccountCard(
                //    navigator = navigator
            )
            profilePaymentServiceCard(
                navHostController = navHostController
            )

            profileYourAddressesCard(
                navHostController = navHostController
            )
            profileDeviceAndAccessCard()
            profileUKCard()
            profileSettingCard()
            profileExit(viewModel = viewModel)
        }


//        if (pullToRefreshState.isRefreshing) {
//            LaunchedEffect(true) {
//                onRefresh()
//            }
//        }
//
//        LaunchedEffect(isRefreshing) {
//            if (isRefreshing) {
//                pullToRefreshState.startRefresh()
//            } else {
//                pullToRefreshState.endRefresh()
//            }
//        }
//
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = Color.White,
            contentColor = ColorCustomResources.colorBazaMainBlue
        )

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }
    }
}

fun LazyListScope.profileNameCard(
    userInfo: UserInfo?,
    viewModel: ProfileScreenViewModel
) {

    item {
        val probel = " "
        val name = userInfo?.data?.profile?.firstName ?: ""
        val lastName = userInfo?.data?.profile?.lastName ?: ""
        val middleName = userInfo?.data?.profile?.middleName ?: ""
        val email = if (userInfo?.data?.profile?.email?.isEmpty() == true) {
            "email не указан"
        } else {
            userInfo?.data?.profile?.email
        }

        val isShowEmailBottomSheet = remember { mutableStateOf(false) }

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//
                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp),
                        imageVector = vectorResource(Res.drawable.ic_profile_card),
                        contentDescription = null,
                        tint = ColorCustomResources.colorBazaMainBlue
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                        text = "$name$probel$lastName$probel$middleName",
                        fontSize = 16.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .height(1.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .clickable(enabled = (email != null && email.isEmpty()),
//                            onClick = {
//
//                            })
                        .clickable {
                            isShowEmailBottomSheet.value = true
                        }
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(36.dp),
                        imageVector = vectorResource(Res.drawable.ic_profile_post_card),
                        contentDescription = null,
                        tint = Color.Gray
                    )

                    Column() {
                        Text(
                            modifier = Modifier
                                .padding(top = 16.dp, end = 16.dp),
                            text = "Email"
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp, end = 16.dp, bottom = 16.dp),
                            text = email ?: ""
                        )
                    }

                    if (email != null) {
                        if (!email.isEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.ic_arrow_right),
                                    contentDescription = "arrow",
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isShowEmailBottomSheet.value) {
            EmailBottomSheet(
                onShowBottomSheet = {
                    isShowEmailBottomSheet.value = it
                },
                viewModel = viewModel
            )
        }
    }
}

fun LazyListScope.profilePhoneNumberCard(
    phone: String
) {
    item {

        val isShowPhoneNumberBottomSheet = remember { mutableStateOf(false) }

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        isShowPhoneNumberBottomSheet.value = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                   // colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                        //    .fillMaxSize()
//                           // .padding(16.dp)
//                            .size(36.dp),
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    // close
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_phone),
                    contentDescription = null,
                    tint = Color.Gray
                )
                //     }


                Column(
                    // modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp),
                        text = "Номер телефона",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 16.dp, bottom = 16.dp),
                        text = phone,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }

        if (isShowPhoneNumberBottomSheet.value) {
            PhoneNumberBottomSheet(
                onShowBottomSheet = {
                    isShowPhoneNumberBottomSheet.value = false
                }
            )
        }
    }
}

fun LazyListScope.profilePersonAccountCard() {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        //openBottomSheet(true)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                    colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clickable {
//                                // openBottomSheet(false)
//                            },
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    // close
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_person_account),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Column(
                    // modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp),
                        text = "Лицевые счета",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 16.dp, bottom = 16.dp),
                        text = "+7 999 999 99 99",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

fun LazyListScope.profilePaymentServiceCard(
    navHostController: NavHostController
) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navHostController.navigate(ScreenRoute.PaymentServiceScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                    colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clickable {
//                                // openBottomSheet(false)
//                            },
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_payment_card),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = "Оплата услуг",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

fun LazyListScope.profileYourAddressesCard(
    navHostController: NavHostController
) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navHostController.navigate(ScreenRoute.AddressScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                    colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clickable {
//                                // openBottomSheet(false)
//                            },
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    // close
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_addresses),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Column(
                    // modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp),
                        text = "Ваши адреса",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 16.dp, bottom = 16.dp),
                        text = "Доступ к Вашим адресам",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

fun LazyListScope.profileDeviceAndAccessCard() {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        //openBottomSheet(true)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                    colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clickable {
//                                // openBottomSheet(false)
//                            },
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    // close
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_devices),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = "Устройства и доступ",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

fun LazyListScope.profileUKCard() {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        //openBottomSheet(true)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                    colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clickable {
//                                // openBottomSheet(false)
//                            },
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    // close
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_chat_with_uk),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = "Связь с Управляющей компанией",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

            }
        }
    }
}

fun LazyListScope.profileSettingCard() {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        //openBottomSheet(true)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

//                Card(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .size(36.dp),
//                    //.align(Alignment.CenterEnd),
//                    shape = RoundedCornerShape(5.dp),
//                    colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clickable {
//                                // openBottomSheet(false)
//                            },
//                        contentAlignment = Alignment.Center,
//                    ) {

                Icon(
                    // close
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp),
                    imageVector = vectorResource(Res.drawable.ic_profile_setting),
                    contentDescription = null,
                    tint = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = "Настройки",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon( // потом исправить на право предупреждение было в ios
                        imageVector = vectorResource(Res.drawable.ic_arrow_right),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

fun LazyListScope.profileExit(
    viewModel: ProfileScreenViewModel
) {
    item {
        val clickState = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    clickState.value = true
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_log_out),
                    contentDescription = "log_out",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp),
                    tint = ColorCustomResources.colorBazaMainRed
                )

                Text(
                    //modifier = Modifier.fillMaxWidth(),
                    text = "Выйти",
                    fontSize = 20.sp
                )
            }
        }

        if (clickState.value) {
            val fingerprint = AuthPlatform().getFingerprint()
            viewModel.logout(fingerprint = fingerprint)
            clickState.value = false
        }
    }
}