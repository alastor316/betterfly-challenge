package com.cadiz.betterfly.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApiSertvice {

    @GET("api/character")
    suspend fun getCharacter(
    ): CharacterJsonResponse

}

private var retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

var service: RickMortyApiSertvice = retrofit.create<RickMortyApiSertvice>(RickMortyApiSertvice::class.java)
