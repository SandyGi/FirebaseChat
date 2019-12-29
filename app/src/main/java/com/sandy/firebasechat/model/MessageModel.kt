package com.sandy.firebasechat.model

import java.io.Serializable

data class MessageModel(
    val author: String,
    val message: String,
    val emoji: String
) : Serializable