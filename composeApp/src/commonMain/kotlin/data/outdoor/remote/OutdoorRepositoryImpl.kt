package data.outdoor.remote

import domain.model.outdoor.Dvr
import domain.repository.OutdoorRepository
import io.ktor.client.HttpClient

class OutdoorRepositoryImpl(
    private val httpClient: HttpClient
) : OutdoorRepository {

    override suspend fun getOutdoors() = getFakeOutdoors()



//    suspend fun getAllMessages(userPairChat: UserPairChat): List<Message> {
//        return try {
////            val response: HttpResponse = httpClient.get(ChatSocketRepository.Endpoints.GetAllMessages.url) {
//            val response: HttpResponse = httpClient.get("") {
//               // contentType(ContentType.Application.Json)
//                //setBody(userPairChat)
//            }
//           // Log.d("4444", " getAllMessages response=" + response.status)
//            val res = response.body<List<MessageDto>>().map {
//                it.mapToDomain()
//            }
//            return res
//        } catch (e: Exception) {
//            println("4444  try catch ChatSocketRepositoryImpl getAllMessages e=" + e)
//            //.d("4444", " try catch ChatSocketRepositoryImpl getAllMessages e=" + e)
//            emptyList()
//        }
//    }




    private fun getFakeOutdoors(): List<Dvr> {
        return listOf(
            Dvr(
                addrId = 263,
                address = "Огородный переулок, 7",
                coordinates = "59.2099862465321 39.8785745028139",
                name = "(baza.net).bazanet.ogorodnyj.pereulok.7.(vhod.v.ofis)-7bf166f80e",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/(baza.net).bazanet.ogorodnyj.pereulok.7.(vhod.v.ofis)-7bf166f80e_720.jpg",
                server = "https://dvr2.baza.net/",
                storageTime = 1209600,
                title = "Огородный переулок, 7",
                token = "3.93rOnEbEAAAAAAAAABIABhUTNfc1lMbzj6ZOYg4VoaJh8QFlo3Vm3VuK",
                videoUrl = "https://dvr.baza.net/vsaas/embed/(baza.net).bazanet.ogorodnyj.pereulok.7.(vhod.v.ofis)-7bf166f80e?autoplay=true&token=3.93rOnEbEAAAAAAAAABIABhUTNfc1lMbzj6ZOYg4VoaJh8QFlo3Vm3VuK&dvr=true"
            ),
            Dvr(
                addrId = 0,
                address = "Вологда ул. Огородный пер. д.10",
                coordinates = "59.209625518891315 39.878586223290895",
                name = "h264.50h20l_18ev200_s38-eaf78b6757",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/h264.50h20l_18ev200_s38-eaf78b6757_720.jpg",
                server = "https://dvr5.baza.net/",
                storageTime = 604800,
                title = "Cam1 Огородный пер. д.10",
                token = "3.93rOnEbEAAAAAAAAABIABhUTNfc1lPet8yUkIpS-qiOpuD_S9F65aIxa",
                videoUrl = "https://dvr.baza.net/vsaas/embed/h264.50h20l_18ev200_s38-eaf78b6757?autoplay=true&token=3.93rOnEbEAAAAAAAAABIABhUTNfc1lPet8yUkIpS-qiOpuD_S9F65aIxa&dvr=true"
            ),
            Dvr(
                addrId = 0,
                address = "Чехова 13",
                coordinates = "59.216582021806346 39.87257872867324",
                name = "(192038).bazanet-3e1e737330",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/(192038).bazanet-3e1e737330_720.jpg",
                server = "https://dvr2.baza.net/",
                storageTime = 604800,
                title = "Чехова 13",
                token = "3.oIB3YtB9AAAAAAAAAE0ABhUTOI3MJiKaPRECDQm8XX8dgCsx0vbMcl1l",
                videoUrl = "https://dvr.baza.net/vsaas/embed/(192038).bazanet-3e1e737330?autoplay=true&token=3.oIB3YtB9AAAAAAAAAE0ABhUTOI3MJiKaPRECDQm8XX8dgCsx0vbMcl1l&dvr=true"
            ),
            Dvr(
                addrId = 0,
                address = "Чехова 13",
                coordinates = "59.216415545526 39.87275310385011",
                name = "(192038).bazanet2-264a0b6810",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/(192038).bazanet2-264a0b6810_720.jpg",
                server = "https://dvr1.baza.net/",
                storageTime = 604800,
                title = "Чехова 13",
                token = "3.oIB3YtB9AAAAAAAAAE0ABhUTOI3MJubF336s51napxFDON5rtByWP3dk",
                videoUrl = "https://dvr.baza.net/vsaas/embed/(192038).bazanet2-264a0b6810?autoplay=true&token=3.oIB3YtB9AAAAAAAAAE0ABhUTOI3MJubF336s51napxFDON5rtByWP3dk&dvr=true"
            ),
            Dvr(
                addrId = 0,
                address = "Чехова 13",
                coordinates = "59.21627277957532 39.87339168943695",
                name = "(192038).bazanet3-f3d08b8514",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/(192038).bazanet3-f3d08b8514_720.jpg",
                server = "https://dvr2.baza.net/",
                storageTime = 604800,
                title = "Чехова 13",
                token = "3.oIB3YtB9AAAAAAAAAE0ABhUTOI3MJvMzCMX6s32TivHtkwDmcxMTNeek",
                videoUrl = "https://dvr.baza.net/vsaas/embed/(192038).bazanet3-f3d08b8514?autoplay=true&token=3.oIB3YtB9AAAAAAAAAE0ABhUTOI3MJvMzCMX6s32TivHtkwDmcxMTNeek&dvr=true"
            ),
            Dvr(
                addrId = 0,
                address = "Вологда Дзержинского 39",
                coordinates = "59.186696146293144 39.92021262645722",
                name = "(192120).cam1.dzerzhinskogo.39-7c72f1f2cc",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/(192120).cam1.dzerzhinskogo.39-7c72f1f2cc_720.jpg",
                server = "https://dvr2.baza.net/",
                storageTime = 604800,
                title = "Cam29 Дзержинского 39",
                token = "3.36jABMCqAAAAAAAAAPoABhUTPFd1stsXrwJrhyW8GOjXDCLahZKZWwIL",
                videoUrl = "https://dvr.baza.net/vsaas/embed/(192120).cam1.dzerzhinskogo.39-7c72f1f2cc?autoplay=true&token=3.36jABMCqAAAAAAAAAPoABhUTPFd1stsXrwJrhyW8GOjXDCLahZKZWwIL&dvr=true"
            ),
            Dvr(
                addrId = 0,
                address = "Вологда Дзержинского 39",
                coordinates = "59.18632218250442 39.921815065421654",
                name = "(192120).cam2.dzerzhinskogo.39-1199e56821",
                oper = "baza",
                previewUrl = "https://s3.baza.net/camera-preview/(192120).cam2.dzerzhinskogo.39-1199e56821_720.jpg",
                server = "https://dvr1.baza.net/",
                storageTime = 604800,
                title = "Cam30 Дзержинского 39",
                token = "3.36jABMCqAAAAAAAAAPoABhUTPFd1siJh9HNLFJmbPXcvIBpIrYc6KDHZ",
                videoUrl = "https://dvr.baza.net/vsaas/embed/(192120).cam2.dzerzhinskogo.39-1199e56821?autoplay=true&token=3.36jABMCqAAAAAAAAAPoABhUTPFd1siJh9HNLFJmbPXcvIBpIrYc6KDHZ&dvr=true"
            )
        )
    }
}