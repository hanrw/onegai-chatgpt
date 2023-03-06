package com.snacks.onegai.chat.internal.api

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

interface ChatBot {
    fun ask(question: Question): Flux<String>
}

@Component
class ChatBotImpl : ChatBot {
    override fun ask(question: Question): Flux<String> {
        return Flux.just("expected")
    }
}

data class Question(
    val content: String,
)
