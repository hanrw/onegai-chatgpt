package com.snacks.onegai.chat.api

import com.snacks.onegai.chat.internal.api.ChatBot
import com.snacks.onegai.chat.internal.api.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class OnegaiChatResources(val chatBot: ChatBot) {

    @GetMapping("/ping")
    fun ping(): String {
        return """
            {
                "response": "pong"
            }
        """.trimIndent()
    }

    @PostMapping("/chat")
    fun chat(@RequestBody request: ChatRequest): Flux<String> {
        return chatBot.chat(Message(request.question))
    }
}
