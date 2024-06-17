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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras.Empty.get
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import di.koinViewModel
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.bazanet_logo_svg
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
//import org.koin.compose.koinInject

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.ui.history_call.HistoryCallScreenViewModel
import util.ColorCustomResources
import util.StartActivity


//class SplashViewModelProvider : KoinComponent {
//    val splashViewModel: SplashViewModel by inject()
//}



@Composable
fun SplashActivityContent(
    onMoveToNextActivity: (StartActivity) -> Unit,
    //viewModel: SplashViewModel = koinViewModel(),
) {

    val viewModel = koinViewModel<SplashViewModel>()
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
