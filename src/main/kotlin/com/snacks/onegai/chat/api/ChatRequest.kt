package com.snacks.onegai.chat.api

/**
 * Request for chat
 */
data class ChatRequest(val question: String)
data class ChatResponse(val answer: String)
