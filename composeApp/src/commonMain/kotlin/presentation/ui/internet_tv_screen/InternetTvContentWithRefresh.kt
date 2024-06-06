package presentation.ui.internet_tv_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import domain.model.home.tariffs_by_location.LocationsTariffsBody
import domain.model.home.tariffs_by_location.PackageTariffCheckable
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import util.ColorCustomResources

@Composable
fun InternetTvContentWithRefresh(
    locationsInternetTv: List<String>,
    navHostController: NavHostController,
    viewModel: InternetTvScreenViewModel = koinInject()
    ) {
    var expanded by remember { mutableStateOf(false) }
    var labelClick by remember { mutableStateOf("г. Вологда") }
    var indexClick by remember { mutableStateOf(-1) }
    var dropdownMenuWidth by remember { mutableStateOf(0) }
    var dropdownMenuHeight by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    val locationTariffsByLocation by viewModel.locationTariffsByLocation.collectAsState()
    val locationTariffsByLocationState = remember { mutableStateOf(locationTariffsByLocation) }

    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val selectedLocationState = remember { mutableStateOf(selectedLocation) }

    val localDensity = LocalDensity.current

    val lazyListState = rememberLazyListState()
    // val locationsInternetTv by viewModel.locationsInternetTv.collectAsStateWithLifecycle()

    LaunchedEffect(labelClick) {
        viewModel.setSelectedLocation(location = labelClick)
        scope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(selectedLocation) {

        selectedLocation?.let {
            val body = LocationsTariffsBody(
                locationId = it.id,
                oper = it.oper
            )
            Logger.d { "4444 body=" + body }
            viewModel.getTariffsByLocation(body)
        } ?: run {
            val body = LocationsTariffsBody(
                locationId = 1,
                oper = "baza"
            )
            Logger.d { "4444 body=" + body }
            viewModel.getTariffsByLocation(body)
        }

    }


    val startListCheckable: MutableList<PackageTariffCheckable> = mutableListOf()
    locationTariffsByLocation.forEach {
        startListCheckable.add(
            PackageTariffCheckable(
                tariff = it,
                ktv = viewModel.selectedLocation.value?.ktv ?: true,
                locationName = viewModel.selectedLocation.value?.name ?: "Вологда"
            )
        )
    }
    viewModel.setListTariffsPackageCheckable(startListCheckable)


//    LaunchedEffect(locationTariffsByLocation) {
//        val startListCheckable: MutableList<PackageTariffCheckable> = mutableListOf()
//        locationTariffsByLocation.forEach {
//            startListCheckable.add(
//                PackageTariffCheckable(
//                    tariff = it,
//                    ktv = viewModel.selectedLocation.value?.ktv ?: true,
//                    locationName = viewModel.selectedLocation.value?.name ?: "Вологда"
//                )
//            )
//        }
//        viewModel.setListTariffsPackageCheckable(startListCheckable)
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
           // .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Выберите город"
        )

        Box {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        dropdownMenuWidth = with(localDensity) {
                            (coordinates.size.width / density).toInt()
                        }
                        dropdownMenuHeight = with(localDensity) {
                            (coordinates.size.height).toInt()
                        }

                    },
                border = BorderStroke(1.dp, ColorCustomResources.colorBazaMainBlue),
                shape = RoundedCornerShape(8.dp),
                //   .weight(1f),
                //.clip(RoundedCornerShape(15)),
                onClick = {
                    expanded = !expanded
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = labelClick,
                        modifier = Modifier
                            //.padding(start = 16.dp)
                            .weight(1f) // Занимает все доступное пространство влево
                    )

                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        //  modifier = Modifier.padding(end = 16.dp) // Отступ справа
                    )
                }

            }

            DropdownMenu(
                modifier = Modifier
                    //.padding(0.dp)
                    .height(400.dp)
                    .width(dropdownMenuWidth.dp)
                    .background(color = Color.White)
                    .shadow(0.dp, shape = RoundedCornerShape(10.dp)),
                //  .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp),
                //  .offset(x = 2.dp),
//                    modifier = Modifier.background(color = Color.Transparent),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(16.dp, 0.dp),
                //scrollState = ,
                content = {
                    locationsInternetTv.forEachIndexed { index, title ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .background(
                                    if (indexClick == index) ColorCustomResources.colorBazaMainBlue
                                    else Color.Transparent
                                )
                                .width(dropdownMenuWidth.dp)
                                .height(40.dp),
                            text = {
                                Text(
                                    text = title,
                                    color = if (indexClick == index) Color.White else Color.Black
                                )
                            },
                            onClick = {
                                expanded = false
                                labelClick = title
                                indexClick = index
                            }
                        )
                    }
                }
            )

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

//                .navigationBarsPadding()
//                .padding(bottom = paddingValue.calculateBottomPadding())
            ,
            state = lazyListState,
            //verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Доступные тарифы",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            items(locationTariffsByLocation) { item ->

                TariffsContent(tariff = item)
            }
        }
    }
}