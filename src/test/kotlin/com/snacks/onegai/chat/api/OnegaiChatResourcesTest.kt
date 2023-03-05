package com.snacks.onegai.chat.api

import com.snacks.onegai.chat.internal.api.ChatBot
import com.snacks.onegai.chat.internal.api.Message
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@WebFluxTest(OnegaiChatResources::class)
class OnegaiChatResourcesTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var chatBot: ChatBot

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
        val request = """
                {
                    "question": "some-question"
                }
        """.trimIndent()

        whenever(chatBot.chat(Message("some-question"))).thenReturn(Flux.just("some-answer"))

        // when
        val result = webTestClient
            .post()
            .uri("/chat")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()

        // then
        result.expectStatus().isOk
            .expectBody().equals("some-answer")
    }
}
