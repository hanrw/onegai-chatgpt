package com.snacks.onegai.chat.api

import com.snacks.onegai.chat.internal.infrastructure.openai.api.StreamChatGPTBot
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@WebFluxTest(OnegaiChatResources::class)
class OnegaiChatResourcesTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var chatBot: StreamChatGPTBot

    @Test
    fun `ping should return pong response when request success`() {
        // given
        val expected = "pong"
        // when
        val result = webTestClient.get().uri("/ping").exchange()
        // then
        result.expectStatus().isOk
            .expectBody()
            .jsonPath("$.response").isEqualTo(expected)
    }

    @Test
    fun `should return answer when get response from chat gpt`() {
        // given
        val request = "some-question"

        whenever(
            chatBot.ask(request),
        ).thenReturn(Flux.just(StreamChatGPTBot.ChatCompletion.dummy()))

        // when
        val result = webTestClient
            .get()
            .uri {
                it.path("/chat")
                    .queryParam("question", request)
                    .build()
            }
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange().expectStatus().isOk

        // then
        StepVerifier.create(result.returnResult(String::class.java).responseBody)
            .expectNext(
                """
                {"answer":"dummy-content"}
                """.trimIndent(),
            )
            .verifyComplete()
    }
}
