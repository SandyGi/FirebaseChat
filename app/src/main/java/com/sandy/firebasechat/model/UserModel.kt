package com.sandy.firebasechat.model

import java.io.Serializable

data class UserModel(val emailId: String, val uid: String, val emoji: String) : Serializable