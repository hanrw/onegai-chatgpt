package com.snacks.onegai.chat.internal.api

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

interface ChatBot {
    fun chat(message: Message): Flux<String>
}

@Component
class ChatBotImpl : ChatBot {
    override fun chat(message: Message): Flux<String> {
        return Flux.just("expected")
    }
}

data class Message(
    val content: String,
)
