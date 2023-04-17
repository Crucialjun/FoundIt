package com.crucialtech.foundit.views

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.crucialtech.foundit.R
import com.crucialtech.foundit.authrepo.AuthViewModel
import com.crucialtech.foundit.databinding.FragmentSignUpBinding
import com.crucialtech.foundit.repository.AuthRepo
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.contracts.contract

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Signupfragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private var _binding: FragmentSignUpBinding? = null

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){result ->
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(idToken,null))
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        oneTapClient = Identity.getSignInClient(requireActivity())

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false).build()
            ).build()

        signInRequest = BeginSignInRequest.builder().setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).setAutoSelectEnabled(true).build()


        binding.txtToSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }



        binding.btnGoogleSignup.setOnClickListener {
            Log.d("TAG", "onViewCreated:tapped ")

            oneTapClient.beginSignIn(signUpRequest).addOnSuccessListener {
                try {
                    activityLauncher.launch(IntentSenderRequest.Builder(it.pendingIntent.intentSender).build())

                }catch (e:Exception){
                    Log.d("TAG", "onViewCreated: Exception ${e.localizedMessage}")
                }
            }
        }

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}