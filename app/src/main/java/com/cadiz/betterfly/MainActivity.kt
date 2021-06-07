package com.cadiz.betterfly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.cadiz.betterfly.character.CharacterFragmentDirections
import com.cadiz.betterfly.character.CharacterListener
import com.cadiz.betterfly.character.CharacterRepository
import com.cadiz.betterfly.databinding.ActivityMainBinding
import com.cadiz.betterfly.login.LoginFragmentDirections
import com.cadiz.betterfly.model.Character
import com.cadiz.betterfly.register.RegisterFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), CharacterListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



    }

    override fun NavigateLoginToListCharacter() {
        findNavController(R.id.main_navigation_container).navigate(LoginFragmentDirections.actionLoginFragmentToChracterFragment())
    }

    override fun NavigateListCharacterToCharacterDetail(character: Character) {
        findNavController(R.id.main_navigation_container).navigate(CharacterFragmentDirections.actionChracterFragmentToCharacterDetailFragment(character))
    }

    override fun NavigateLoginToRegister() {
        findNavController(R.id.main_navigation_container).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    override fun NavigateRegisterToListCharacter() {
        findNavController(R.id.main_navigation_container).navigate(RegisterFragmentDirections.actionRegisterFragmentToCharacterFragment())
    }


}