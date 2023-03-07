package com.snacks.onegai.chat.internal.infrastructure.openai.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.snacks.onegai.chat.internal.api.ChatBot
import com.snacks.onegai.chat.internal.infrastructure.openai.internal.api.ComplicationRequest
import com.snacks.onegai.chat.internal.infrastructure.openai.internal.api.StreamChatGPTClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

interface StreamChatGPTBot : ChatBot<String, Flux<StreamChatGPTBot.ChatCompletion>> {
    override fun ask(question: String): Flux<ChatCompletion>
    data class ChatDelta(
        val content: String?,
    )

    data class ChatChoice(
        val delta: ChatDelta,
        val index: Int,
        @JsonProperty("finish_reason")
        val finishReason: String?,
    )

    data class ChatCompletion(
        val id: String,
        val `object`: String,
        val created: Long,
        val model: String,
        val choices: List<ChatChoice>,
    ) {
        companion object {
            fun dummy(): ChatCompletion {
                return ChatCompletion(
                    "dummy-id",
                    "dummy-object",
                    0,
                    "dummy-model",
                    listOf(
                        ChatChoice(
                            ChatDelta("dummy-content"),
                            0,
                            "dummy-finish-reason",
                        ),
                    ),
                )
            }
        }
    }
}

@Component
class DefaultStreamChatGPTBot(private val client: StreamChatGPTClient, private val objectMapper: ObjectMapper) : StreamChatGPTBot {
    override fun ask(question: String): Flux<StreamChatGPTBot.ChatCompletion> {
        val response = client.ask(ComplicationRequest(listOf(ComplicationRequest.Message(question))))
        // Create a Flux of ChatCompletion objects by filtering and mapping the incoming Flux data
        return response
            .filter { isValid(it) }
            .map { objectMapper.readValue(it, StreamChatGPTBot.ChatCompletion::class.java) }
    }

    private fun isValid(it: String) = it != "[DONE]"
}
