package presentation.ui.splash_activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.bazanet_logo_svg
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import util.ColorCustomResources
import util.StartActivity


//@Composable
//fun saveFireBaseTokenTemp(viewModel: SplashViewModel) {
//    Logger.d("4444 вызван saveFireBaseTokenTemp")
//    // В этом методе вы можете отправить токен уведомления на сервер.
//    NotifierManager.addListener(object : NotifierManager.Listener {
//        override fun onNewToken(token: String) {
//            Logger.d("4444 FirebaseOnNewToken: $token")
//
//            //AppLogger.d("FirebaseOnNewToken: $token")
//        }
//    })
//}


@Composable
fun SplashActivityContent(
    onMoveToNextActivity: (StartActivity) -> Unit,
    viewModel: SplashViewModel = koinInject(),
) {
    val nextScreen by viewModel.nextScreen.collectAsStateWithLifecycle()



   // saveFireBaseTokenTemp(viewModel = viewModel)






    LaunchedEffect(nextScreen) {
        nextScreen?.let {
            delay(1000L)
            onMoveToNextActivity(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp, 153.85.dp),
            imageVector = vectorResource(Res.drawable.bazanet_logo_svg),
            contentDescription = "",
            tint = ColorCustomResources.colorBazaMainBlue
        )
    }

    // Запускаем метод viewModel.method() только один раз при инициализации компонента
    LaunchedEffect(Unit) {
        viewModel.checkAndUpdateToken()
    }

}
