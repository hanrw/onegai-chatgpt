package com.snacks.onegai.chat.internal.infrastructure.openai

import com.snacks.onegai.chat.internal.api.ChatBot
import com.snacks.onegai.chat.internal.api.Question
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange
import reactor.core.publisher.Flux

interface ChatGPTBot : ChatBot {
    @PostExchange("/ask")
    override fun ask(@RequestBody question: Question): Flux<String>

    @PostExchange("/empty")
    fun emptyPost(): Flux<String>

    @GetExchange("/ping")
    fun ping(): String
}
