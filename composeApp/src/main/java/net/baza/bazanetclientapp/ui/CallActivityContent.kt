package net.baza.bazanetclientapp.ui

//@Composable
//fun CallActivityContent(callActivity: CallActivity) {
//    Column(
//        modifier = Modifier
//            .background(Color.White)
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Spacer(modifier = Modifier.weight(3f))
//
//        Image(
//            painter = painterResource(Res.drawable.call_activity_logo),
//            modifier = Modifier
//                .size(width = 156.dp, height = 120.dp),
//            contentDescription = null
//        )
//
//        Text(
//            modifier = Modifier
//                .padding(16.dp),
//            text = "Личный кабинет",
//            fontSize = 30.sp
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(400.dp)
//        ) {
//            ElevatedCard(
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//            ) {
//                ViewPagerAuth(callActivity)
//            }
//        }
//
//        Spacer(modifier = Modifier.weight(0.5f))
//        Text(
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//                .padding(16.dp),
//            text = "Версия четкая"
//        )
//    }
//}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ViewPagerAuth(callActivity: CallActivity) {
//    val scope = rememberCoroutineScope()
//    val pagerState = rememberPagerState(pageCount = { TabsAuth.entries.size })
//    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }
//
//    Column(
//        modifier = Modifier
//            .background(Color.White)
//            .fillMaxWidth()
//    ) {
//        TabRow(
//            selectedTabIndex = selectedTabIndex.value,
//            modifier = Modifier
//                .fillMaxWidth(),
//            indicator = { tabPositions ->
//                TabRowDefaults.Indicator(
//                    color = ColorCustomResources.colorBazaMainBlue, // Здесь укажите желаемый цвет для горизонтальной линии
//                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value])
//                )
//            }
//        ) {
//            TabsAuth.entries.forEachIndexed { index, currentTab ->
//                Tab(
//                    modifier = Modifier.background(Color.White),
//                    selected = selectedTabIndex.value == index,
//                    selectedContentColor = Color.Black,
//                    unselectedContentColor = MaterialTheme.colorScheme.outline,
//                    onClick = {
//                        scope.launch {
//                            pagerState.animateScrollToPage(currentTab.ordinal)
//                        }
//                    },
//                    text = {
//                        Text(
//                            text = currentTab.text,
//                            fontSize = 16.sp
//                        )
//                    }
//                )
//            }
//        }
//
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier
//                .fillMaxWidth()
//        ) { page ->
//            when (page) {
//                0 -> LoginByPhoneNumber(callActivity)
//                1 -> LoginByWiFi(callActivity)
//            }
//
//        }
//    }
//}
//
//@Composable
//fun LoginByPhoneNumber(callActivity: CallActivity) {
//    var textLastNameState by remember { mutableStateOf("") }
//    var textNameState by remember { mutableStateOf("") }
//    var textMiddleNameState by remember { mutableStateOf("") }
//    var isFocusTextFiled by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ) {
//
//        Text(
//            modifier = Modifier
//                //.fillMaxWidth()
//                //.align(Alignment.CenterHorizontally)
//                .padding(top = 20.dp, bottom = 20.dp),
//            text = "Введите номер телефона"
//        )
//
//        TextFieldHelper(
//            value = textLastNameState,
//            onValueChanged = { textLastNameState = it },
//            hintText = "Номер запили"
//        )
//
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            ElevatedButton(
//                modifier = Modifier
//                    // .fillMaxWidth()
//                    .padding(top = 30.dp, bottom = 16.dp),
////                .shadow(2.dp, RoundedCornerShape(2.dp)),
//                shape = RoundedCornerShape(8.dp),
//
//                onClick = {
//                    moveToMainActivity(callActivity)
//                },
//                content = {
//                    Text(
//                        modifier = Modifier
//                            .padding(start = 16.dp, end = 16.dp),
//                        text = "Далее") },
//                colors = ButtonDefaults.buttonColors(
//                    contentColor = Color.White,
//                    containerColor = ColorCustomResources.colorBazaMainBlue
//                ),
//                //shape = RoundedCornerShape(10.dp)
//            )
//        }
//
//    }
//}
//
//@Composable
//fun LoginByWiFi(callActivity: CallActivity) {
//    var numberContractState by remember { mutableStateOf("") }
//    var isFocusTextFiled by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 10.dp)
//            .fillMaxWidth()
//            .background(Color.White),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 20.dp, bottom = 16.dp),
//            text = "Если Вы являетесь нашим абонентом, возможен вход по Вашей сети WI-FI"
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//                //.padding(start = 26.dp, end = 26.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//
//            ElevatedButton(
//                modifier = Modifier
//                    .padding(top = 20.dp, bottom = 8.dp),
////                .shadow(2.dp, RoundedCornerShape(2.dp)),
//                shape = RoundedCornerShape(8.dp),
//
//                onClick = {
//                    moveToMainActivity(callActivity)
//                },
//                content = {
//                    Text(
//                    modifier = Modifier
//                        .padding(start = 16.dp, end = 16.dp),
//                    text = "Войти по Wi-Fi") },
//                colors = ButtonDefaults.buttonColors(
//                    contentColor = Color.White,
//                    containerColor = ColorCustomResources.colorBazaMainBlue
//                ),
//                //shape = RoundedCornerShape(10.dp)
//            )
//
//        }
//    }
//}
//
//fun moveToMainActivity(callActivity: CallActivity) {
//    val intent = Intent(callActivity, MainActivity::class.java)
//    callActivity.startActivity(intent)
//}
//
//enum class TabsAuth(
//    val text: String,
//) {
//    TabOneAuth(text = "Вход по номеру телефона"),
//    TabTwoAuth(text = "Вход по WI-FI")
//}
//
//////////////////////////////////////////////////////////////////////////
//
//@Composable
//fun TutorialInputMaskScreen() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
//    ) {
//        TutorialManualVisualTransformation()
//    }
//}
//
//
//
//@Composable
//fun TutorialManualVisualTransformation() {
//    class ManualVisualTransformation(val enableCursorMove: Boolean) : VisualTransformation {
//        override fun filter(text: AnnotatedString): TransformedText {
//            val inputText = text.text // "1111"
//            // "(XX) XXXXX - XXXX"
//            // "(XXX) XXX - XXXX" // мой
//            val formattedText = when(inputText.length) {
//                in (1..3) -> "($inputText"
//                in (4..6) -> "(${inputText.substring(0, 3)}) ${inputText.substring(3)}"
//                in (7..10) ->  {
//                    "(${inputText.substring(0, 3)}) ${inputText.substring(3, 6)} - ${inputText.substring(6)}"
//                }
//                else -> ""
//            }
//
//            val offsetTranslator = object : OffsetMapping {
//                override fun originalToTransformed(offset: Int): Int {
//                    // offset = input.count/original at cursor position -- "(11) 1|" offset = 3
//                    val transformedCursor = when (offset) {
//                        in (1..3) -> offset + 1 // "(XX|" offset = 2, sep = 1, result = 3
//                        in (4..6) -> offset + 3 // "(XX) X|" offset = 3, sep = 3, result = 6
//                        in (7..10) -> offset + 6
//                        else -> offset
//                    }
//
//                    return if (enableCursorMove) transformedCursor // formattedText
//                    else formattedText.length // "(XX) XXXXX - XXXX" return 17
//                }
//
//                override fun transformedToOriginal(offset: Int): Int {
//                    // offset = formatted.length/transformed at cursor position -- "(11) 1|" offset = 6
//                    val originalCursor = when (offset) {
//                        in (1..3) -> offset - 1 // "(XX|" offset = 3, sep = 1, result = 2
//                        in (4..10) -> offset - 3 // "(XX) X|" offset = 6, sep = 3, result = 3
//                        in (11..17) -> offset - 6
//                        else -> offset
//                    }
//
//                    return if (enableCursorMove) originalCursor // inputs
//                    else inputText.length // "(XX) XXXXX - XXXX" return 11
//                }
//            }
//            return TransformedText(AnnotatedString(formattedText), offsetTranslator)
//        }
//    }
//
//    var inputText by remember { mutableStateOf("") }
//    val mask = "(XXX) XXX - XXXX"
//    val maskCharInput = 'X'
//    val inputMaxLength = mask.count { maskChar -> maskChar == maskCharInput }
//
//    TextField(
//        value = inputText,
//        onValueChange = { currentText ->
//            inputText = currentText.take(inputMaxLength).filter { it.isDigit() }
//        },
//        label = { Text(text = "Manual") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//        visualTransformation = ManualVisualTransformation(enableCursorMove = true)
//    )
//}
