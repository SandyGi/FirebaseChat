package com.sandy.firebasechat.ui.view

interface LoginView{
    fun logTheUserIn(username: String, uid: String, emoji: String)
    fun onFailure()
    fun spinProgressBar()
    fun stopProgressBar()
}