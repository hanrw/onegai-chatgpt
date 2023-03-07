package com.snacks.onegai.chat.internal.infrastructure.openai.internal.infrastructure.httpclient

import com.snacks.onegai.chat.internal.infrastructure.openai.internal.api.StreamChatGPTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration
class ClientConfiguration {
    @Value("\${chatbots.openai.chatgpt.bearer-token}")
    private lateinit var bearerToken: String

    @Value("\${chatbots.openai.chatgpt.base-url}")
    private lateinit var baseUrl: String

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .defaultHeaders {
                it.setBearerAuth(bearerToken)
            }.baseUrl(baseUrl)
            .build()
    }

    @Bean
    fun proxyFactory(webClient: WebClient): HttpServiceProxyFactory {
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
            .build()
    }

    @Bean
    fun chatGPTBot(proxyFactory: HttpServiceProxyFactory): StreamChatGPTClient {
        return proxyFactory.createClient()
    }
}
