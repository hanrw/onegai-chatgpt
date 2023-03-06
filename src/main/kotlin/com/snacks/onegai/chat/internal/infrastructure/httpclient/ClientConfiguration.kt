package com.snacks.onegai.chat.internal.infrastructure.httpclient

import com.snacks.onegai.chat.internal.infrastructure.openai.ChatGPTBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration
class ClientConfiguration {
    @Bean
    fun proxyFactory(webClient: WebClient): HttpServiceProxyFactory {
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
            .build()
    }

    @Bean
    fun chatGPTBot(proxyFactory: HttpServiceProxyFactory): ChatGPTBot {
        return proxyFactory.createClient()
    }
}
