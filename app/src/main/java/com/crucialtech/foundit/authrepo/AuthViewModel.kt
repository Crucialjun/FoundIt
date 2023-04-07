package com.crucialtech.foundit.authrepo

import android.content.Context
import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crucialtech.foundit.SignInResult
import com.crucialtech.foundit.repository.AuthRepo
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel :ViewModel() {
    private val _signUpResultState =  MutableStateFlow(SignUpResultState())
    val signUpResultState = _signUpResultState.asStateFlow()

    fun signUpResult(result : SignInResult){
        _signUpResultState.update {
            it.copy(
                isSuccess = result.data != null,
                signError = result.errorMsg
            )
        }
    }

    fun resetSignUpState(){
        _signUpResultState.update { SignUpResultState() }
    }
    suspend fun signUpWithOneTap(context: Context): IntentSender? {
        val oneTapClient = Identity.getSignInClient(context)
        return AuthRepo(context,oneTapClient).signInWithGoogle()

    }
}

data class SignUpResultState(
    val isSuccess : Boolean = false,
            val signError : String? = null)