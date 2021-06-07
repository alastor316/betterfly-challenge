package com.cadiz.betterfly.character

import android.util.Log
import com.cadiz.betterfly.api.CharacterJsonResponse
import com.cadiz.betterfly.api.service
import com.cadiz.betterfly.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository {


    suspend  fun fetchCharacters(): MutableList<Character> {
        return withContext(Dispatchers.IO){
            val repoJsonResponse : CharacterJsonResponse = service.getCharacter()
            val repoList = parseResult(repoJsonResponse)
            repoList
        }
    }

    private fun parseResult(ghJsonResponse: CharacterJsonResponse): MutableList<Character> {
        val characterList: MutableList<Character> = mutableListOf<Character>()

        val itemList: List<Character> = ghJsonResponse.results


        for (item: Character in itemList) {

            val id = item.id
            val name = item.name
            val status = item.status
            val species = item.species
            val gender = item.gender
            val image = item.image

            characterList.add(
                    Character(
                        id,
                        name,
                       status,
                        species,
                        gender,
                        image
                    )
            )
        }
        return characterList
    }
}