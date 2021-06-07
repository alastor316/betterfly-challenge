package com.cadiz.betterfly.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.cadiz.betterfly.api.ApiResponseStatus
import com.cadiz.betterfly.model.User
import kotlinx.coroutines.launch
import java.net.UnknownHostException


private val Tag = RegisterViewModel::class.java.simpleName

class RegisterViewModel : ViewModel() {

    private val registerRepository = RegisterRepository()

    var authenticatedUserLiveData: LiveData<User>? = null
    var createdUserLiveData: LiveData<User>? = null





    fun register(email: String, password: String, firstName: String) {
        viewModelScope.launch {
            try {
                authenticatedUserLiveData = registerRepository.registerFirebase(email, password, firstName)
            } catch (e: UnknownHostException) {
                Log.d(Tag, "No internet connection", e)
            }
        }
   }


    fun createUser(authenticatedUser: User) {
        viewModelScope.launch {
            try {
                createdUserLiveData = registerRepository.createUserInFirestoreIfNotExists(authenticatedUser)
            } catch (e: UnknownHostException) {
                Log.d(Tag, "No internet connection", e)
            }
        }
    }

}