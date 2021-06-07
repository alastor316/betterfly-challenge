package com.cadiz.betterfly.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cadiz.betterfly.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()


    fun loginFirebase(emailText: String, passwordText: String): MutableLiveData<User> {
        val authenticatedUserMutableLiveData = MutableLiveData<User>()
        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser = authTask.result!!.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val email = firebaseUser.email
                        val name = firebaseUser.email
                        val user = User(uid, email, name)
                        user?.isNew = isNewUser
                        authenticatedUserMutableLiveData.setValue(user)
                    }
                } else {
                    Log.d("FirebaseAuthAppTag", authTask.exception?.message!!)
                }
            }

        return authenticatedUserMutableLiveData
    }
}