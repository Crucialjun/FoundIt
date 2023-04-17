package com.crucialtech.foundit.authrepo

import android.content.Context
import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crucialtech.foundit.SignInResult
import com.crucialtech.foundit.repository.AuthRepo
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel(private val authRepository: AuthRepo) :ViewModel() {

fun refreshToken(){
    authRepository.refreshToken()
}


    data class SignUpResultState(
        val isSuccess: Boolean = false,
        val signError: String? = null
    )

}