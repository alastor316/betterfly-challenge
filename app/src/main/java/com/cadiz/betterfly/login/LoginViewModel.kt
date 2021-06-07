package com.cadiz.betterfly.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cadiz.betterfly.api.ApiResponseStatus
import com.cadiz.betterfly.character.CharacterViewModel
import com.cadiz.betterfly.model.User
import com.cadiz.betterfly.register.RegisterRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.net.UnknownHostException

private val Tag = LoginViewModel::class.java.simpleName

class LoginViewModel : ViewModel() {

   /* private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private val _firebaseAuth = MutableLiveData<FirebaseAuth>()
    val firebaseAuth: LiveData<FirebaseAuth>
        get() = _firebaseAuth*/

    private val loginRepository = LoginRepository()


    var loginUserLiveData: LiveData<User>? = null


   /* init {
        viewModelScope.launch {
            try {
                _firebaseAuth.value = FirebaseAuth.getInstance()
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                Log.d(Tag, "No internet connection", e)
            }

        }
    }*/


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                loginUserLiveData = loginRepository.loginFirebase(email, password)
            } catch (e: UnknownHostException) {
                Log.d(Tag, "No internet connection", e)
            }
        }
    }
}