package com.crucialtech.foundit.auth.repository

import com.crucialtech.foundit.R
import com.crucialtech.foundit.models.AppUser
import com.google.firebase.firestore.FirebaseFirestore




class DatabaseRepo {
    var db = FirebaseFirestore.getInstance()

    fun addUserToDatabase(user : AppUser){

        db.collection(R.string.users_database_table.toString()).document(user.uid).set(user)
    }

}