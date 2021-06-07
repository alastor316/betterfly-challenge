package com.cadiz.betterfly.model

import com.google.firebase.database.Exclude
import java.io.Serializable

class User (val uid: String,val name: String,val email: String) :Serializable {


    @Exclude var isAuthenticated :Boolean = false
    @Exclude var isNew: Boolean = false
    @Exclude var isCreated: Boolean = false


}