package com.sandy.firebasechat.presenter.interf

interface LoginInteractor {
    fun attemptToLogIn(email: String, password: String)
    fun createUser(user: String, emoji: String): Map<String, Any>
}