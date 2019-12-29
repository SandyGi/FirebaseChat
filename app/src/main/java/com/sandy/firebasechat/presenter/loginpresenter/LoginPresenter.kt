package com.sandy.firebasechat.presenter.loginpresenter

import com.firebase.client.*
import com.sandy.firebasechat.model.UserModel
import com.sandy.firebasechat.presenter.interf.LoginInteractor


class LoginPresenter(fbPresenter: FirebaseLoginPresenter) : LoginInteractor {

    private var userRef = Firebase("https://<your-firebase>/Users/")
    private var presenter = fbPresenter
    override fun attemptToLogIn(email: String, password: String) {
        userRef.authWithPassword(email, password, object : Firebase.AuthResultHandler {
            override fun onAuthenticated(authData: AuthData) {
                userRef = Firebase("https://<your-firebase>/Users/" + authData.getUid());
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(firebaseError: FirebaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(UserModel::class.java)
                        val loggedUser =
                            Firebase("https://<your-firebase>/currentUsers/" + authData.uid)
                        loggedUser.setValue(createUser(user.emailId, user.emoji))
                        presenter.onSuccess(user.emailId, authData.uid, user.emoji)
                    }

                })
            }

            override fun onAuthenticationError(p0: FirebaseError?) {
                presenter.onFailure()
            }

        })
    }

    override fun createUser(user: String, emoji: String): Map<String, Any> {
        val userToCreate = HashMap<String, Any>()
        userToCreate["username"] = user
        userToCreate["emoji"] = emoji
        return userToCreate
    }

}