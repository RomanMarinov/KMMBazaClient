package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val clientIp: String,
    val companyReview: List<CompanyReview>?,
    val contacts: Contacts?,
    val faq: List<Faq>?,
    val ipInfo: String,
    val links: Links?,
    val locations: List<Location>?,
    val mapDefault: List<MapDefault>?,
    val mapMarkers: MapMarkers?,
    val mapServers: List<String>?,
    val mobileTariffs: List<MobileTariff>?,
    val payForward: List<PayForward>?,
    val payment_methods: List<PaymentMethod>?,
    val promoInfo: List<PromoInfo>?,
    val requirements: Requirements?,
    val serverTimeInt: Int,
    val serverTimeString: String,
    val showMobileTariffs: Boolean?,
    val termsOfService: TermsOfService,
    val validateCallNumber: String,
    val vendors: Vendors
)