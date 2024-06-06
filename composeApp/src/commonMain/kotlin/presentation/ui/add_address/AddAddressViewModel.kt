package presentation.ui.add_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import data.add_address.remote.dto.AddAddressResponseDTO
import data.add_address.remote.dto.CheckAddressResponseDTO
import domain.add_address.AddAddressBody
import domain.add_address.AddAddressResponse
import domain.add_address.CheckAddressBody
import domain.add_address.CheckAddressData
import domain.add_address.Data
import domain.add_address.service_request.ServiceRequestBody
import domain.repository.AddAddressRepository
import domain.repository.UserInfoRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import util.Constants
import util.ScreenRoute

class AddAddressViewModel(
    private val addAddressRepository: AddAddressRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private var _addresses: MutableStateFlow<List<CheckAddressData>> = MutableStateFlow(emptyList())
    val addresses: StateFlow<List<CheckAddressData>> = _addresses

    private var _errorNetwork: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val errorNetwork: StateFlow<Boolean> = _errorNetwork

    private var _addAddressResponse: MutableStateFlow<AddAddressResponse?> = MutableStateFlow(null)
    val addAddressResponse: StateFlow<AddAddressResponse?> = _addAddressResponse

    private var _requestNumber: MutableStateFlow<Int> = MutableStateFlow(Constants.RequestResultTicketId.DEFAULT_INT)
    val requestNumber: StateFlow<Int> = _requestNumber

    private var fullName = ""
    private var profilePhone = ""

    init {
        getFullNameAndPhone()
    }

    fun checkAddress(addressText: String) {
        // список из viewModel.addresses приходит нормально на 3 символ
        val checkAddressBody = CheckAddressBody(text = addressText)
        viewModelScope.launch(Dispatchers.IO) {
            val response = addAddressRepository.checkAddress(checkAddressBody)

            Logger.d("4444 address=" + response)

            response?.let {
                if (it.status.isSuccess()) {
                    val result = it.body<CheckAddressResponseDTO>()
                    result.mapToDomain().data?.let { listAddress ->
                        _addresses.value = listAddress
                    }
                } else {
                    _errorNetwork.value = true
                }
            }
        }
    }

    fun getAddressById(
        addressId: Int,
        oper: String,
        address: String,
        flat: String
    ) {
        Logger.d("4444 make getAddressById")
        viewModelScope.launch(Dispatchers.IO) {
            val response = addAddressRepository.getAddressById(
                AddAddressBody(
                    addrId = addressId,
                    oper = oper,
                    address = address,
                    flat = flat
                )
            )

            Logger.d("4444 response=" + response?.body())

            val result = response?.body<AddAddressResponseDTO>()
            _addAddressResponse.value = result?.mapToDomain()
        }
    }

    fun resetFlowAddAddressResponse() {
        _addAddressResponse.value = null
    }

    private fun getFullNameAndPhone() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = userInfoRepository.getUserInfo().data.profile
            fullName = StringBuilder()
                .append(profile.lastName)
                .append(" ")
                .append(profile.firstName)
                .append(" ")
                .append(profile.middleName)
                .toString()

            profilePhone = profile.phone.toString()
        }
    }

    suspend fun sendRequest(
        navigationFrom: String,
        inputTextPhoneNumber: String,
        dataAddress: Data?) {
        _requestNumber.value = Constants.RequestResultTicketId.FAILURE_INT

        when(navigationFrom) {
            ScreenRoute.OutdoorScreen.route -> {
                val comment = "ТЕСТ ПРОГРАММИСТЫ НЕ ЗВОНИТЬ Заявка на подключение Дворовой камеры (номер телефона, привязанный к аккаунту: $profilePhone)"
                val body = ServiceRequestBody(
                    phone = inputTextPhoneNumber,
                    city = dataAddress?.city,
                    street = dataAddress?.street,
                    home = dataAddress?.home,
                    flat = dataAddress?.flat.toString(),
                    name = fullName,
                    dvr = true,
                    comment = comment
                )
                val ticketId = addAddressRepository.sendServiceRequest(body = body)
                ticketId?.let {
                    _requestNumber.value = it
                }
            }
            ScreenRoute.DomofonScreen.route,
            ScreenRoute.ProfileScreen.route -> {
                val comment = "ТЕСТ ПРОГРАММИСТЫ НЕ ЗВОНИТЬ Заявка на подключение Умновго домофона (номер телефона, привязанный к аккаунту: $profilePhone)"
                val body = ServiceRequestBody(
                    phone = inputTextPhoneNumber,
                    city = dataAddress?.city,
                    street = dataAddress?.street,
                    home = dataAddress?.home,
                    flat = dataAddress?.flat.toString(),
                    name = fullName,
                    domophone = true,
                    comment = comment
                )
                val ticketId = addAddressRepository.sendServiceRequest(body = body)
                ticketId?.let {
                    _requestNumber.value = it
                }
            }
        }
    }
}