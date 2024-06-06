package util

//object SnackBarOpenDoor {
//    @Composable
//    fun open() {
//        SnackbarHost(
//            hostState = snackbarHostState,
//            snackbar = {
//                Snackbar(
//                    modifier = Modifier
//                        .padding(8.dp),
////                    dismissAction = {
////                        Logger.d { " 4444 2 snackBarState.value=" + snackBarState.value }
////                        snackBarState.value = false
////                    },
//                    actionOnNewLine = true,
//                    action = {
//                        Button(modifier = Modifier.padding(8.dp), onClick = {
//                            snackbarHostState.currentSnackbarData?.dismiss()
//                        }) {
//                            Text("Да понятно")
//                        }
//                    },
//                    shape = RoundedCornerShape(20.dp),
//                ) {
//                    Text(text = "Написать реализацию для создания ярлыка")
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter).navigationBarsPadding()
//// .padding(paddingValue)
//        )
//    }
//}