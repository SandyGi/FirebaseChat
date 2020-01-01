package com.sandy.firebasechat.model

import java.io.Serializable

data class MessageModel(
    val id: String,
    val message: String,
    val fromId: String,
    val toId: String,
    val timeStamp: Long
) : Serializable{
    constructor(): this("", "","","", -1)
}