package com.snacks.onegai.chat.internal.infrastructure.openai.internal.api

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import reactor.core.publisher.Flux

@HttpExchange(
    url = "/v1/chat",
    accept = [MediaType.TEXT_EVENT_STREAM_VALUE],
)
interface StreamChatGPTClient {
    @PostExchange("/completions")
    fun ask(@RequestBody question: ComplicationRequest): Flux<ServerSentEvent<String>>

    @GetExchange("/ping")
    fun ping(): String
}

data class ComplicationRequest(
    val messages: List<Message>,
    val model: Model = Model.GPT_3_5_TURBO,
    val stream: Boolean = true,

) {
    companion object {
        fun Message(question: String): Message {
            return Message(question, Role.USER)
        }
    }
}

enum class Model {
    @JsonProperty("gpt-3.5-turbo")
    GPT_3_5_TURBO,
}

data class Message(
    val content: String,
    val role: Role = Role.USER,
)

enum class Role {
    @JsonProperty("user")
    USER,
}

data class StreamComplicationResponse(
    val id: String,
    val `object`: String,
    val created: Int,
    val choices: List<Choice>,
    val usage: Usage,
) {
    companion object {
        fun dummy(): StreamComplicationResponse {
            return StreamComplicationResponse(
                id = "some-id",
                `object` = "some-object",
                created = 1,
                choices = listOf(
                    Choice(
                        index = 0,
                        message = Message("pong"),
                        finishReason = "some-reason",
                    ),
                ),
                usage = Usage(
                    promptTokens = 1,
                    completionTokens = 1,
                    totalTokens = 1,
                ),
            )
        }
    }
}

data class Choice(
    val index: Int,
    val message: Message,
    @JsonProperty("finish_reason")
    val finishReason: String,
)

data class Usage(
    @JsonProperty("prompt_tokens")
    val promptTokens: Int,
    @JsonProperty("completion_tokens")
    val completionTokens: Int,
    @JsonProperty("total_tokens")
    val totalTokens: Int,
)
