package data.add_address.remote

import co.touchlab.kermit.Logger
import data.add_address.remote.dto.AddAddressBodyDTO
import data.add_address.remote.dto.AddressDeleteResponseDTO
import data.add_address.remote.dto.service_request.ServiceRequestBodyDTO
import data.add_address.remote.dto.service_request.ServiceRequestResponseDTO
import domain.add_address.AddAddressBody
import domain.add_address.CheckAddressBody
import domain.add_address.service_request.ServiceRequestBody
import domain.repository.AddAddressRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess


class AddAddressRepositoryImpl(
    private val httpClient: HttpClient
) : AddAddressRepository {

    override suspend fun checkAddress(checkAddressBody: CheckAddressBody): HttpResponse? {
        val checkAddressBodyDTO = checkAddressBody.mapToData()

        val response = httpClient.post("public/check_address") {
            contentType(ContentType.Application.Json)
            setBody(body = checkAddressBodyDTO)
        }
        return response
    }

    override suspend fun getAddressById(addAddressBody: AddAddressBody): HttpResponse? {
        val addAddressBodyDTO = AddAddressBodyDTO(
            addrId = addAddressBody.addrId,
            oper = addAddressBody.oper,
            address = addAddressBody.address,
            flat = addAddressBody.flat
        )
        val id = addAddressBody.addrId

        return try {
            val response = httpClient.post("user/address/$id") {
                contentType(ContentType.Application.Json)
                setBody(body = addAddressBodyDTO)
            }
            if (response.status.isSuccess()) {
//               logManager.writeLogToDB("Успешно получили инфо по адресу c addrId = '${body.addrId}'")
            } else {
//               logManager.writeLogToDB("Ошибка получения инфо по адресу c addrId = '${body.addrId}'")
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.d("4444 try catch getAddressById e=" + e)
            //logManager.writeLogToDB("Ошибка получения инфо по адресу c addrId = '${body.addrId}': ${e.javaClass.simpleName}")
            //logManager.writeLogToDB(e.stackTraceToString())
            null
        }
    }


    override suspend fun deleteAddress(id: Int): Boolean? {
        Logger.d("4444  deleteAddress id=" + id)
        return try {
            val response = httpClient.delete("user/address/$id") {
                contentType(ContentType.Application.Json)
            }
            return if (response.status.isSuccess()) {
//            logManager.writeLogToDB("Успешно удалили адрес с id = $id")
                val result = response.body<AddressDeleteResponseDTO>().mapToDomain()


                result.data.result ?: true
            } else {
                //logManager.writeLogToDB("Ошибка удаления адреса с id = $id")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.d("4444 try catch deleteAddress e=" + e)
            //logManager.writeLogToDB("Ошибка получения инфо по адресу c addrId = '${body.addrId}': ${e.javaClass.simpleName}")
            //logManager.writeLogToDB(e.stackTraceToString())
            null
        }

    }

    /////////////////////////
    // этот в какой момент сработал
    override suspend fun uploadImage(imageByteArray: ByteArray, id: Int): HttpResponse? {
        Logger.d("uploadImage imageByteArray size=${imageByteArray.size} id=$id")
        return try {
            val response: HttpResponse = httpClient.submitFormWithBinaryData(
                url = "verification/address/$id",
                formData = formData {
                    append("description", "Ktor logo")
                    append("image", imageByteArray, Headers.build {
                        append(HttpHeaders.ContentType, "application/octet-stream")
                        append(
                            HttpHeaders.ContentDisposition,
                            "form-data; name=\"image\"; filename=\"image.png\""
                        )
                    })
                }
            )
            if (response.status.isSuccess()) {
                Logger.d("4444 uploadImage OK")
                val code = response.status
                val successContent = response.body<String>()
                val headers = response.headers
                val description = response.status.description
                Logger.d("4444 uploadImage OK code=$code")
                Logger.d("4444 uploadImage OK successContent=$successContent")
                Logger.d("4444 uploadImage OK headers=$headers")
                Logger.d("4444 uploadImage OK description=$description")
            } else {
                Logger.d("4444 uploadImage FAILURE")
                val code = response.status
                val errorContent = response.body<String>()
                val headers = response.headers
                val description = response.status.description
                Logger.d("4444 uploadImage FAILURE code=$code")
                Logger.d("4444 uploadImage FAILURE errorContent=$errorContent")
                Logger.d("4444 uploadImage FAILURE headers=$headers")
                Logger.d("4444 uploadImage FAILURE description=$description")
            }
//            response
            response
        } catch (e: Exception) {
            // Обработка ошибки, если нужно
            Logger.d(" try catch 4444 uploadImage e=" + e)
            e.printStackTrace()
            null
        }
    }

    override suspend fun sendServiceRequest(body: ServiceRequestBody): Int? {
        val serviceRequestDTO: ServiceRequestBodyDTO = body.mapToData()
        return try {
//        logManager.writeLogToDB("Отправляем заявку на подключение для авторизованного абонента")
            val response = httpClient.post("public/connection_request") {
                contentType(ContentType.Application.Json)
                setBody(body = serviceRequestDTO)
            }
            //       ServiceRequestResponse
            if (response.status.isSuccess()) {
                val result = response.body<ServiceRequestResponseDTO>()
                return result.mapToDomain().data.ticketId
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //logManager.writeLogToDB("Ошибка отправки заявки на подключение для авторизованного абонента: ${e.javaClass.simpleName}")
            //logManager.writeLogToDB(e.stackTraceToString())
            null
        }

    }


//    override suspend fun uploadImage(imageByteArray: ByteArray, id: Int): HttpResponse? {
//        Logger.d("uploadImage imageByteArray size=${imageByteArray.size} id=$id")
//        return try {
//////////////////////////////
////            val response: HttpResponse = httpClient.submitFormWithBinaryData(
////                url = "verification/address/$id",
////                formData = formData {
////                    append("description", "Ktor logo1")
////                    append("image", imageByteArray, Headers.build {
////                        append(HttpHeaders.ContentType, "application/octet-stream")
////                        append(HttpHeaders.ContentDisposition, "form-data; name=\"image1\"; filename=\"image1.png\"")
////                    })
////                }
////            )
/////////////////////////////
//
////            val res = InputByFilepath().getByteArray(imageByteArray)
////            val response: HttpResponse = httpClient.submitFormWithBinaryData(
////                url = "verification/address/$id",
////                formData = formData {
////                    append("description", "Ktor logo")
////                    append("image", res, Headers.build {
////                        append(HttpHeaders.ContentType, "image/png")
////                        append(HttpHeaders.ContentDisposition, "filename=\"ktor_logo.png\"")
////                    })
////                }
////            )
///////////////////
//          //  val res = InputByFilepath().getByteArray()
//            val data: List<PartData> = formData {
//                appendInput(
//                    key = "yourKey",
//                    block = { res },
//                    headers = Headers.build {
//                        append(
//                            HttpHeaders.ContentType,
//                            ContentType.Application.OctetStream.toString()
//                        )
//                        append(
//                            HttpHeaders.ContentDisposition, ContentDisposition.File
//                                .withParameter(ContentDisposition.Parameters.FileName, "fileName")
//                                .toString()
//                        )
//                    }
//                )
//            }
//            val response = httpClient.submitFormWithBinaryData(
//                url = "verification/address/$id",
//                formData = data)
//
//
//
//
////            val response = httpClient.post("verification/address/{$id}") {
////                contentType(ContentType.Application.OctetStream)
////                setBody(body = imageByteArray)
////            }
//            if (response.status.isSuccess()) {
//                Logger.d("4444 uploadImage OK")
//            } else {
//                Logger.d("4444 uploadImage FAILURE")
//                val code = response.status
//                val errorContent = response.body<String>()
//                val headers = response.headers
//                val description = response.status.description
//                Logger.d("4444 uploadImage FAILURE code=$code")
//                Logger.d("4444 uploadImage FAILURE errorContent=$errorContent")
//                Logger.d("4444 uploadImage FAILURE headers=$headers")
//                Logger.d("4444 uploadImage FAILURE description=$description")
//            }
////            response
//            response
//        } catch (e: Exception) {
//            // Обработка ошибки, если нужно
//            Logger.d(" try catch 4444 uploadImage e=" + e)
//            e.printStackTrace()
//            null
//        }
//    }

    //////////////////////////


//    override suspend fun uploadImage(imageByteArray: ByteArray, id: Int): HttpResponse? {
//        Logger.d("uploadImage imageByteArray=" + imageByteArray + " id=" + id)
//
//
//        val fileName = "fileName"
//
//        return try {
//            val response = httpClient.post("verification/address/{$id}") {
//                setBody(body = MultiPartFormDataContent(
//                    formData {
//                        append(
//                            "document",
//                            imageByteArray,
//                            Headers.build {
//                                append(HttpHeaders.ContentType, "images/*") // Mime type required
//                                append(HttpHeaders.ContentDisposition, "filename=$fileName") // Filename in content disposition required
//                            }
//                        )
//                    }
//                ))
//            }
//            if (response.status.isSuccess()) {
//                Logger.d("4444 uploadImage OK")
//            } else {
//                Logger.d("4444 uploadImage NOT OK")
//            }
//            response
//
//
//
//
////            val response = httpClient.post("verification/address/{$id}") {
////                contentType(ContentType.Application.OctetStream)
////                setBody(body = imageByteArray)
////            }
////            if (response.status.isSuccess()) {
////                Logger.d("4444 uploadImage OK")
////            } else {
////                Logger.d("4444 uploadImage NOT OK")
////            }
////            response
//        } catch (e: Exception) {
//            // Обработка ошибки, если нужно
//            Logger.d(" try catch 4444 uploadImage e=" + e)
//            e.printStackTrace()
//            null
//        }
//    }

    ////////////////////////

//    override suspend fun getAddressById(
//        body: AddAddressBody
//    ): Response<AddAddressResponse>? {
//        logManager.writeLogToDB("Отправляем запрос на получение инфо по адресу c addrId = '${body.addrId}'")
//        Log.i("CHECK_ADDRESS", "Отправляем запрос на получение инфо по адресу c addrId = '${body.addrId}': DomofonRepositoryImpl/getAddressById()")
//        return try {
//            apiService.getAddressById(body, body.addrId).also {
//                if (it.isSuccessful) {
//                    logManager.writeLogToDB("Успешно получили инфо по адресу c addrId = '${body.addrId}'")
//                } else {
//                    logManager.writeLogToDB("Ошибка получения инфо по адресу c addrId = '${body.addrId}'")
//                }
//                return it
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            logManager.writeLogToDB("Ошибка получения инфо по адресу c addrId = '${body.addrId}': ${e.javaClass.simpleName}")
//            logManager.writeLogToDB(e.stackTraceToString())
//            null
//        }
//    }


//    suspend fun uploadImage(token: String, byteArray: ByteArray) {
//        httpClient.submitFormWithBinaryData(
//            url(MainEndpoints.UPDATE_PROFILE_PIC),
//            formData {
//                append("photo", byteArray, Headers.build {
//                    println("Original size ${byteArray.size} bytes")
//                    append(HttpHeaders.ContentType, "image/jpg")
//                    append(HttpHeaders.ContentDisposition, "filename=image.jpg")
//                }
//                )
//            }) {
//            header("Authorization", token)
//            onUpload { bytesSentTotal, contentLength ->
//                println("Sent $bytesSentTotal bytes from $contentLength")
//            }
//        }
//    }

//        suspend fun sendPhoto() {
//            val response: HttpResponse = httpClient.submitFormWithBinaryData(
//                url = "http://localhost:8080/upload",
//                formData = formData {
//                    append("description", "Ktor logo")
//                    append("image", File("ktor_logo.png").readBytes(), Headers.build {
//                        append(HttpHeaders.ContentType, "image/png")
//                        append(HttpHeaders.ContentDisposition, "filename=\"ktor_logo.png\"")
//                    })
//                }
//            )
//        }
}