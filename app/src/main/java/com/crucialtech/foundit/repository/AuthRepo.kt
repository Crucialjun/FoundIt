package com.crucialtech.foundit.repository

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crucialtech.foundit.R
import com.crucialtech.foundit.SignInResult
import com.crucialtech.foundit.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthRepo(
) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signInResult = MutableLiveData<String>()
    val signInResult: LiveData<String> get() = _signInResult

    fun getCurrentSignedInUser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                uid = uid,
                username = displayName,
                picUrl = photoUrl.toString()
            )
        };
    }



    fun refreshToken() {
        // Refresh the user's token to keep the session active
        auth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Token refreshed successfully
                } else {
                    // Failed to refresh token
                }
            }
    }


}



