//package util
//
//import kotlinx.coroutines.flow.StateFlow
//
//
//class ConnectivityLiveDataHelper(val context: Context) : StateFlow<NetworkState>() {
//
//    private val connectivityManager: ConnectivityManager =
//        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private var networkReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            notifyNetworkStatus()
//        }
//    }
//
//    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
//    private var wasConnectionLost = false
//
//    override fun onActive() {
//        super.onActive()
//        notifyNetworkStatus()
//        when {
//            // for devices above Nougat
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
//                getConnectivityManagerCallback()
//            )
//
//            // for devices b/w Lollipop and Nougat
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
//
//            //below lollipop
//            else -> {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    context.registerReceiver(
//                        networkReceiver,
//                        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
//                    )
//                }
//            }
//        }
//    }
//
//    override fun onInactive() {
//        // When all observers are gone, remove connectivity callback or un register the receiver.
//        super.onInactive()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            connectivityManager.unregisterNetworkCallback(networkCallback)
//        } else {
//            context.unregisterReceiver(networkReceiver)
//        }
//    }
//
//    private fun lollipopNetworkAvailableRequest() {
//        val builder = NetworkRequest.Builder()
//            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
//            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
//        connectivityManager.registerNetworkCallback(
//            builder.build(),
//            getConnectivityManagerCallback()
//        )
//    }
//
//    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            networkCallback = object : ConnectivityManager.NetworkCallback() {
//                override fun onAvailable(network: Network) {
//                    postValue(NetworkState.CONNECTED)
//                }
//
//                override fun onLost(network: Network) {
//                    postValue(NetworkState.DISCONNECTED)
//                }
//            }
//            return networkCallback
//        } else {
//            throw IllegalAccessError("Should not have happened")
//        }
//    }
//
//    private fun notifyNetworkStatus() {
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        if (activeNetwork?.isConnected == true) {
//            postValue(NetworkState.CONNECTED)
//        } else {
//            postValue(NetworkState.DISCONNECTED)
//            wasConnectionLost = true
//        }
//    }
//}