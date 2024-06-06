//package di.network_module
//
//import io.ktor.client.HttpClient
//import io.ktor.client.HttpClient
//import io.ktor.client.HttpClientConfig
//import io.ktor.client.features.HttpRequestBuilder
//import io.ktor.client.features.HttpResponseContainer
//import io.ktor.client.features.HttpResponsePipeline
//import io.ktor.client.features.expectSuccess
//import io.ktor.client.features.Plugin
//import io.ktor.client.features.auth.Auth
//import io.ktor.client.request.HttpRequestPipeline
//import io.ktor.http.HttpStatusCode
//import io.ktor.client.request.HttpRequestData
//import io.ktor.client.request.takeFrom
//import io.ktor.client.statement.HttpResponse
//import io.ktor.util.AttributeKey
//
//class TokenRefreshFeature {
//    companion object Feature : HttpClient.Feature<Feature, TokenRefreshFeature> {
//        override val key: AttributeKey<TokenRefreshFeature> = AttributeKey("TokenRefreshFeature")
//
//        override fun prepare(block: Feature.() -> Unit): TokenRefreshFeature = TokenRefreshFeature()
//
//        var tokenRefreshBlock: (HttpClient) -> Unit = {}
//
//        private const val AUTHORIZATION_HEADER = "Authorization"
//    }
//
//    fun HttpRequestBuilder.appendToken(token: String) {
//        headers.append(AUTHORIZATION_HEADER, "Bearer $token")
//    }
//}
//
//fun HttpClient.tokenRefreshFeature(block: TokenRefreshFeature.() -> Unit) {
//    install(TokenRefreshFeature, block)
//}
//
//fun HttpClient.config(block: HttpRequestBuilder.() -> Unit): HttpRequestBuilder {
//    val builder = HttpRequestBuilder()
//    block(builder)
//    return builder
//}
//
//fun HttpClient.refreshToken() {
//    TokenRefreshFeature.tokenRefreshBlock(this)
//}