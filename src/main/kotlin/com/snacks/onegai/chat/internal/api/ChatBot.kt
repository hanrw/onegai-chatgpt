package com.snacks.onegai.chat.internal.api

interface ChatBot<Q, R> {
    fun ask(question: Q): R
}
