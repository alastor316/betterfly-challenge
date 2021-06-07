package com.cadiz.betterfly.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cadiz.betterfly.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class RegisterRepository {


    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef: CollectionReference = rootRef.collection("users")


    fun registerFirebase(emailText: String, passwordText: String, firstName: String): MutableLiveData<User> {
      val authenticatedUserMutableLiveData = MutableLiveData<User>()
      firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser = authTask.result!!.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firstName
                        val email = emailText
                        val user = User(uid, name, email)
                        user?.isNew = isNewUser
                        authenticatedUserMutableLiveData.setValue(user)
                    }
                } else {
                    Log.d("FirebaseAuthAppTag", authTask.exception?.message!!)
                }
            }

        return authenticatedUserMutableLiveData
    }


    fun createUserInFirestoreIfNotExists(authenticatedUser: User): MutableLiveData<User> {
        val newUserMutableLiveData = MutableLiveData<User>()
        val uidRef = usersRef.document(authenticatedUser.uid)
        uidRef.get()
            .addOnCompleteListener { uidTask: Task<DocumentSnapshot> ->
                if (uidTask.isSuccessful) {
                    val document = uidTask.result
                    if (!document!!.exists()) {
                        uidRef.set(authenticatedUser)
                            .addOnCompleteListener { userCreationTask: Task<Void?> ->
                                if (userCreationTask.isSuccessful) {
                                    authenticatedUser.isCreated = true
                                    newUserMutableLiveData.setValue(authenticatedUser)
                                } else {
                                    Log.d("FirebaseAuthAppTag", uidTask.exception?.message!!)

                                }
                            }
                    } else {
                        newUserMutableLiveData.setValue(authenticatedUser)
                    }
                } else {
                    Log.d("FirebaseAuthAppTag", uidTask.exception?.message!!)
                }
            }
        return newUserMutableLiveData
    }
}