package com.sandy.firebasechat.presenter.loginpresenter

import com.sandy.firebasechat.presenter.interf.LoginInteractor

class LoginPresenter(fbPresenter: FirebaseLoginPresenter) : LoginInteractor {

    private var presenter = fbPresenter
    override fun attemptToLogIn(email: String, password: String) {

    }

    override fun createUser(user: String, emoji: String): Map<String, Any> {
        val userToCreate = HashMap<String, Any>()
        userToCreate["username"] = user
        userToCreate["emoji"] = emoji
        return userToCreate
    }

}