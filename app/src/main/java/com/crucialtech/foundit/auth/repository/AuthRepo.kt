package com.crucialtech.foundit.auth.repository


import android.content.Context
import android.content.IntentSender
import android.util.Log
import com.crucialtech.foundit.R
import com.crucialtech.foundit.UserData
import com.crucialtech.foundit.models.AppUser
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthRepo {

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
    fun signInWithGoogleCredential(token: String?){
        auth.signInWithCredential(GoogleAuthProvider.getCredential(token,null)).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user = task.result?.user

                val appUser = AppUser(
                    email = user?.email ?: "",
                    uid = user?.uid ?: "",
                    name = user?.displayName ?: "",
                    phone = user?.phoneNumber ?: "",
                    profileImgUrl = user?.photoUrl.toString(),
                    username = user?.displayName ?: ""
                )

                DatabaseRepo().addUserToDatabase(appUser)


                Log.d("TAG", "signInWithGoogleCredential: success")
            }else{
                Log.d("TAG", "signInWithGoogleCredential: failed")
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



