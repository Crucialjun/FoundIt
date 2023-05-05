package com.crucialtech.foundit.repository


import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crucialtech.foundit.R
import com.crucialtech.foundit.SignInResult
import com.crucialtech.foundit.UserData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.CancellationException

class AuthRepo(
) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private val callbackManager = CallbackManager.Factory.create()

    fun getCurrentSignedInUser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                uid = uid,
                username = displayName,
                picUrl = photoUrl.toString()
            )
        };
    }

    suspend fun signUpWithGoogleIntent(oneTapClient: SignInClient,context: Context) : IntentSender?{
        signUpRequest = BeginSignInRequest.builder().setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).setAutoSelectEnabled(true).build()

        val result = try {
            oneTapClient.beginSignIn(signUpRequest).await()
        }catch(e : Exception){
            if(e is CancellationException){
                Log.d("TAG", "signUpWithGoogleIntent: error is ${e.localizedMessage}")
                throw  e
            }
            Log.d("TAG", "signUpWithGoogleIntent: error is ${e.localizedMessage}")
            null
        }
        Log.d("TAG", "signUpWithGoogleIntent: result is $result");
        return result?.pendingIntent?.intentSender;

    }

    suspend fun signInWithGoogleIntent(oneTapClient: SignInClient,context: Context) : IntentSender?{
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false).build()
            ).build()
        val result = try {
            oneTapClient.beginSignIn(signUpRequest).await()
        }catch(e : Exception){
            if(e is CancellationException){
                throw  e
            }
            null
        }
        return result?.pendingIntent?.intentSender;

    }

    suspend fun  loginWithFacebookIntent(activity: Activity){
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email","public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager,object:FacebookCallback<LoginResult>{
            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException) {
                TODO("Not yet implemented")
            }

            override fun onSuccess(result: LoginResult) {
                signInFacebookCredential(result.accessToken)
            }

        })

    }

    suspend fun signInWithGoogleCredential(token: String?){
        auth.signInWithCredential(GoogleAuthProvider.getCredential(token,null))
    }

     fun signInFacebookCredential(token: AccessToken){
        auth.signInWithCredential(FacebookAuthProvider.getCredential(token.token)).addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val user = auth.currentUser

            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", it.exception)

            }
        }
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



