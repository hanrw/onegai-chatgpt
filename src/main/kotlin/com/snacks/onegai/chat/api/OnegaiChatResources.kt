package com.snacks.onegai.chat.api

import com.snacks.onegai.chat.internal.infrastructure.openai.api.StreamChatGPTBot
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class OnegaiChatResources(val streamChatGPTBot: StreamChatGPTBot) {
    @GetMapping("/ping")
    fun ping(): String {
        return """
            {
                "response": "pong"
            }
        """.trimIndent()
    }

    @GetMapping("/chat", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun chat(@RequestParam question: String): Flux<ChatResponse> {
        return streamChatGPTBot.ask(question).flatMapIterable {
            it.choices
        }.mapNotNull {
            it.delta.content?.let { content -> ChatResponse(content) }
        }
    }
}
