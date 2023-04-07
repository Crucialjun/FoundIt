package com.crucialtech.foundit.repository

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
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
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private var auth: FirebaseAuth = Firebase.auth

    fun getCurrentSignedInUser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                uid = uid,
                username = displayName,
                picUrl = photoUrl.toString()
            )
        };
    }

    suspend fun signInWithGoogle(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(googleOneTapSignInRequest()).await()
        }catch (e :Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent : Intent) : SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try{
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        uid = uid,
                        username = displayName,
                        picUrl = photoUrl.toString()
                    )
                }, errorMsg = null )
        }catch (e :Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(data = null, errorMsg = e.message.toString())
        }
    }

    private fun googleOneTapSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false).build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }



    suspend fun signOut(){
        try{
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e :Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }


}