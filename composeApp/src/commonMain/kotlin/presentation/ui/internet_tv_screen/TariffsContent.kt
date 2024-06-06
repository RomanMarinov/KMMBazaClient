package presentation.ui.internet_tv_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import domain.model.home.tariffs_by_location.DataTariffs
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_internet_tv
import kmm.composeapp.generated.resources.ic_internet_wi_fi
import kmm.composeapp.generated.resources.promo_flag
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.internet_tv_screen.model.Price
import util.ColorCustomResources

@Composable
fun TariffsContent(tariff: DataTariffs) {
    Logger.d { "444tariff=" + tariff }

//    val bitrate: Int? = null, -
//    val channels: Int? = null, -
//    val durationMonth: Int? = null, -
//    val name: String, -
//    val packagePrice: Int? = null, // когда кабель отключ то
//    val prepayPrice: Int? = null, // не
//    val price: Int,
//    val sale: Int? = null, -
//    val type: String  // ktv(меленький) и inet (большой)

    val nameState = remember { mutableStateOf(tariff.name) }
    val bitrateState = remember { mutableStateOf(tariff.bitrate?.div(1000)) }
    val channelsState = remember { mutableStateOf(tariff.channels) }
    val packagePriceState = remember { mutableStateOf(tariff.packagePrice) }
    val prepayPriceState = remember { mutableStateOf(tariff.prepayPrice) }
    val priceState = remember { mutableStateOf(tariff.price) }
    val saleState = remember { mutableStateOf(tariff.sale) }
    val typeState = remember { mutableStateOf(tariff.type) }


    val checkedState = remember { mutableStateOf(true) }
    val prices = getPrices(tariff = tariff, checkedState = checkedState.value)

    // есть еще ktv для него другой макет
    // для него скрывать строку Интернет
    // чек бокс
    // скидку


    Card(
        modifier = Modifier
            .fillMaxWidth()
         .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
    ) {
        Column(
            modifier = Modifier
                // .background(ColorCustomResources.colorBazaMainBlue)

                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ColorCustomResources.colorTariffMain,
                            ColorCustomResources.colorTariffCenter
                        )
                    )
                )

                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                    text = tariff.name,
                    color = Color.White,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.weight(1f))


                if (tariff.type == "inet" && tariff.sale != null) {


                    Box(
                        modifier = Modifier
                            //        .weight(1f)
                            .padding(end = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(60.dp),
                            imageVector = vectorResource(Res.drawable.promo_flag),
                            contentDescription = null,
                            tint = ColorCustomResources.colorDiscount
                        )

                        Text(
                            color = Color.White,
                            text = "${tariff.sale}%"
                        )
                    }
                }
            }

            if (tariff.type == "inet") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_internet_wi_fi),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = "Интернет",
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(end = 8.dp),
                                text = tariff.bitrate?.div(1000).toString(),
                                color = Color.White,
                                fontSize = 30.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(end = 8.dp),
                                text = "Мбит/с",
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                    imageVector = vectorResource(Res.drawable.ic_internet_tv),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = "Кабельное ТВ",
                    color = Color.White
                )

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val channels = if (channelsState.value == null) {
                            "120"
                        } else {
                            channelsState.value.toString()
                        }

                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = channels,
                            color = Color.White,
                            fontSize = 30.sp
                        )
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = "Каналов",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }

            if (tariff.type == "inet") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = Color.White,
                            checkmarkColor = ColorCustomResources.colorBazaMainBlue,
                            checkedColor = Color.White
                        )
                    )

                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = "В пакет включено кабельное ТВ",
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(1.dp)
            )


            if (tariff.type == "inet" && tariff.sale != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            // .fillMaxWidth()
                            //.background(ColorCustomResources.colorBazaMainRed)
                            .padding(start = 8.dp, end = 8.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                // .fillMaxWidth()
                                .background(ColorCustomResources.colorBazaMainRed),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(ColorCustomResources.colorBazaMainRed),
                                text = "Скидка",
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        color = Color.White,
                        text = "Успейте подключиться и получите скидку 50% на ${tariff.durationMonth} месяца(ев)"
                    )
                }
            }



            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {


                // if (prices.oldPrice.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough),
                    text = prices.oldPrice, color = Color.White
                )
                //}


//                val price = if (tariff.type == "ktv") {
//                    tariff.price.toString()
//                } else if (tariff.type == "inet" && checkedState.value) {
//                    tariff.packagePrice.toString()
//                } else {
//                    tariff.price.toString()
//                }


//                val price = if (checkedState.value) {
//                    tariff.packagePrice.toString()
//                } else {
//                    tariff.price.toString()
//                }

                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = prices.price, color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = "Р/мес", color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                ElevatedButton(
                    modifier = Modifier
                        // .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {

                    },
                    content = { Text("Подключить") },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = ColorCustomResources.colorBazaMainBlue,
                        containerColor = Color.White
                    )
                )
            }
        }
    }
}

fun getPrices(tariff: DataTariffs, checkedState: Boolean): Price {

    Logger.d { "4444 getPrices tariff=" + tariff + " checkedState=" + checkedState }

    val discount = tariff.sale ?: 0

    if (tariff.packagePrice == null) {
        if (discount > 0) {
            return if (checkedState) {
                Price(
                    oldPrice = tariff.packagePrice.toString(),
                    price = ((tariff.packagePrice ?: 0) * discount).div(100).toString()
                )
            } else {
                Price(
                    oldPrice = tariff.price.toString(),
                    price = (tariff.price * discount).div(100).toString()
                )
            }
        } else {
            return Price(
                oldPrice = "",
                price = tariff.price.toString()
            )
        }
    } else {
        if (checkedState) {
            if (discount > 0) {
                return if (checkedState) {
                    Price(
                        oldPrice = tariff.packagePrice.toString(),
                        price = ((tariff.packagePrice ?: 0) * discount).div(100).toString()
                    )
                } else {
                    Price(
                        oldPrice = tariff.price.toString(),
                        price = (tariff.price * discount).div(100).toString()
                    )
                }
            } else {
                return Price(
                    oldPrice = "",
                    price = tariff.packagePrice.toString()
                )
            }
        } else {
            if (discount > 0) {
                return if (checkedState) {
                    Price(
                        oldPrice = tariff.packagePrice.toString(),
                        price = ((tariff.packagePrice ?: 0) * discount).div(100).toString()
                    )
                } else {
                    Price(
                        oldPrice = tariff.price.toString(),
                        price = (tariff.price * discount).div(100).toString()
                    )
                }
            } else {
                return Price(
                    oldPrice = "",
                    price = tariff.price.toString()
                )
            }
        }
    }
}