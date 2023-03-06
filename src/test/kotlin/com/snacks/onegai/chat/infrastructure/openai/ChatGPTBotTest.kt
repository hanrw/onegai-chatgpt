package com.snacks.onegai.chat.infrastructure.openai

import com.snacks.onegai.chat.internal.api.Question
import com.snacks.onegai.chat.internal.infrastructure.httpclient.ClientConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Answers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ExtendWith(MockitoExtension::class)
class ChatGPTBotTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var webClient: WebClient

    @Test
    fun `should return pong when ping success`() {
        // given
        val chatGPTBot = ClientConfiguration().chatGPTBot(ClientConfiguration().proxyFactory(webClient))

        whenever(
            webClient
                .method(HttpMethod.GET)
                .uri("/ping", emptyMap<String, Any>())
                .retrieve()
                .bodyToMono(any<ParameterizedTypeReference<String>>()),
        ).thenReturn(Mono.just("pong"))

        // when
        val pong = chatGPTBot.ping()

        // then
        assertThat(pong).isEqualTo("pong")
    }

    @Test
    fun `should return pong when get response from chat gpt`() {
        // given
        val chatGPTBot = ClientConfiguration().chatGPTBot(ClientConfiguration().proxyFactory(webClient))

        whenever(
            webClient
                .method(HttpMethod.POST)
                .uri("/empty", emptyMap<String, Any>())
                .retrieve()
                .bodyToFlux(any<ParameterizedTypeReference<String>>()),
        ).thenReturn(Flux.just("pong"))

        // when
        val r = chatGPTBot.emptyPost()

        // then
        assertThat(r.blockFirst()).isEqualTo("pong")
    }

    @Test
    fun `should return answer when get response from chat gpt`() {
        // given
        val chatGPTBot = ClientConfiguration().chatGPTBot(ClientConfiguration().proxyFactory(webClient))

        val question = Question("hello")
        whenever(
            webClient
                .method(HttpMethod.POST)
                .uri("/ask", emptyMap<String, Any>())
                .retrieve()
                .bodyToFlux(any<ParameterizedTypeReference<String>>()),
        ).thenReturn(Flux.just("pong"))

        // when
        val r = chatGPTBot.ask(question)

        // then
        assertThat(r.blockFirst()).isEqualTo("pong")
    }
}