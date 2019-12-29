package com.sandy.firebasechat.presenter.loginpresenter

import com.sandy.firebasechat.presenter.interf.FirebaseLoginInteractor
import com.sandy.firebasechat.ui.view.LoginView


class FirebaseLoginPresenter(val view: LoginView) : FirebaseLoginInteractor {
    private var loginView: LoginView = view
    private var loginPresenter = LoginPresenter(this)

    override fun receiveUserLogin(email: String, password: String) {
        loginView.spinProgressBar()
        loginPresenter.attemptToLogIn(email, password)
    }

    override fun onFailure() {
        loginView.stopProgressBar()
        loginView.onFailure()
    }

    override fun onSuccess(user: String, uid: String, emoji: String) {
        loginView.stopProgressBar()
        loginView.logTheUserIn(user, uid, emoji)
    }

}