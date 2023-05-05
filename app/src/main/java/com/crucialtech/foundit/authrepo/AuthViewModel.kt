package com.crucialtech.foundit.authrepo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crucialtech.foundit.SignInResult
import com.crucialtech.foundit.repository.AuthRepo
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel

class AuthViewModel @Inject constructor(private val authRepository:AuthRepo) :ViewModel() {




    fun refreshToken(){
    authRepository.refreshToken()
}

    suspend fun signUpWithGoogle(oneTapClient: SignInClient, context: Context): IntentSender? {
        return authRepository.signUpWithGoogleIntent(oneTapClient,context)
    }

    suspend fun signinwithCredential(token : String?){
        authRepository.signInWithGoogleCredential(token)
    }

     fun signInWithFacebookCredential(token: AccessToken){
        authRepository.signInFacebookCredential(token)
    }


}