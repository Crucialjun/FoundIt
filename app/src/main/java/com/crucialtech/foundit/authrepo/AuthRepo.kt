package com.crucialtech.foundit.authrepo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthRepo {
    private  var auth: FirebaseAuth = Firebase.auth

    fun checkIfUserIsSignedIn(): FirebaseUser? {
        return auth.currentUser;
    }


}