package com.cadiz.betterfly.character

import com.cadiz.betterfly.model.Character

interface CharacterListener {
    fun NavigateLoginToListCharacter()
    fun NavigateListCharacterToCharacterDetail(character: Character)
    fun NavigateLoginToRegister()
    fun NavigateRegisterToListCharacter()
}