package com.sandy.firebasechat.presenter.interf

interface FirebaseLoginInteractor {
     fun receiveUserLogin(email: String, password: String)
     fun onFailure()
     fun onSuccess(user: String, uid: String, emoji: String)
}