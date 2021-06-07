package com.cadiz.betterfly.character

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cadiz.betterfly.api.ApiResponseStatus
import com.cadiz.betterfly.model.Character
import kotlinx.coroutines.launch
import java.net.UnknownHostException

private val Tag = CharacterViewModel::class.java.simpleName

class CharacterViewModel(application: Application): AndroidViewModel(application) {


    private val character = CharacterRepository()
    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private var _chararcterList = MutableLiveData<MutableList<Character>>()
    val chararcterList: LiveData<MutableList<Character>>
        get() = _chararcterList

    init {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _chararcterList.value = character.fetchCharacters()
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NOT_INTERNET_CONNECTION
                Log.d(Tag, "No internet connection", e)
            }

        }
    }
}