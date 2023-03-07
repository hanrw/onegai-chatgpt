package com.snacks.onegai.chat.infrastructure.openai.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.snacks.onegai.chat.internal.infrastructure.openai.api.DefaultStreamChatGPTBot
import com.snacks.onegai.chat.internal.infrastructure.openai.api.StreamChatGPTBot
import com.snacks.onegai.chat.internal.infrastructure.openai.internal.api.ComplicationRequest
import com.snacks.onegai.chat.internal.infrastructure.openai.internal.api.StreamChatGPTClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux

@ExtendWith(MockitoExtension::class)
class DefaultStreamChatGPTBotTest {
    @InjectMocks
    lateinit var bot: DefaultStreamChatGPTBot

    @Mock
    lateinit var client: StreamChatGPTClient

    @Mock
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `ask should return chat completion when get response from chat gpt`() {
        // given:
        val question = "What is your name?"
        whenever(client.ask(ComplicationRequest(listOf(ComplicationRequest.Message(question))))).thenReturn(Flux.just("some-data", "[DONE]"))
        val chatCompletion = StreamChatGPTBot.ChatCompletion.dummy()
        whenever(
            objectMapper.readValue(
                eq("some-data"),
                eq(StreamChatGPTBot.ChatCompletion::class.java),
            ),
        ).thenReturn(chatCompletion)

        // when:
        val result = bot.ask(question).blockFirst()

        // then:
        assertThat(result).isEqualTo(chatCompletion)
    }
}
