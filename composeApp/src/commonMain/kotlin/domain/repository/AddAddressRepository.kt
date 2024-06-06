package domain.repository

import domain.add_address.AddAddressBody
import domain.add_address.CheckAddressBody
import domain.add_address.service_request.ServiceRequestBody
import io.ktor.client.statement.HttpResponse

interface AddAddressRepository {
    suspend fun checkAddress(checkAddressBody: CheckAddressBody): HttpResponse?
    suspend fun getAddressById(addAddressBody: AddAddressBody): HttpResponse?
    suspend fun deleteAddress(id: Int): Boolean?


    suspend fun uploadImage(imageByteArray: ByteArray, id: Int): HttpResponse?

    suspend fun sendServiceRequest(body: ServiceRequestBody): Int?
}